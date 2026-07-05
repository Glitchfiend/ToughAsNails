/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.item;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.temperature.TemperatureHelperImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TemperatureProperty implements RangeSelectItemModelProperty
{
    public static final MapCodec<TemperatureProperty> CODEC = RecordCodecBuilder.mapCodec((builder) ->
            builder.group(
                    Codec.unboundedMap(Codec.INT, Delta.CODEC)
                            .fieldOf("deltas")
                            .forGetter(TemperatureProperty::deltas)
            ).apply(builder, TemperatureProperty::new)
    );

    private final Map<Integer, Delta> deltas;

    public TemperatureProperty(Map<Integer, Delta> deltas)
    {
        this.deltas = Maps.newHashMap(deltas);
    }

    public TemperatureProperty() { this.deltas = new HashMap<>(); }

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int i)
    {
        if (owner == null)
            return 0.5F;

        Entity holder = (Entity)owner;

        if (level == null && owner.level() instanceof ClientLevel)
            level = (ClientLevel)holder.level();

        if (level == null)
            return 0.5F;

        Delta delta = deltas.computeIfAbsent(holder.getId(), k -> new Delta());
        delta.update(level, getTemperatureForThermometer(level, holder));
        return delta.getValue();
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type()
    {
        return CODEC;
    }

    private Map<Integer, Delta> deltas()
    {
        return this.deltas;
    }

    private static TemperatureLevel getTemperatureForThermometer(Level level, Entity holder)
    {
        TemperatureLevel temperatureLevel = TemperatureHelper.getTemperatureAtPos(level, holder.blockPosition());

        // Use the player to acquire nearby thermoregulators, even if they aren't holding the thermometer
        Player player = Minecraft.getInstance().player;
        Set<BlockPos> nearbyThermoregulators = TemperatureHelper.getTemperatureData(player).getNearbyThermoregulators();
        return TemperatureHelperImpl.modifyTemperatureByThermoregulators(level, nearbyThermoregulators, holder.blockPosition(), temperatureLevel);
    }

    private static class Delta
    {
        public static final Codec<Delta> CODEC = RecordCodecBuilder.create((builder) ->
            builder.group(
                    Codec.LONG.fieldOf("last_update_tick").forGetter(Delta::lastUpdateTick),
                    Codec.DOUBLE.fieldOf("value").forGetter(Delta::currentValue),
                    Codec.DOUBLE.fieldOf("rota").forGetter(Delta::rota)
            ).apply(builder, Delta::new)
        );

        private long lastUpdateTick;
        private double currentValue;
        private double rota;

        public Delta(long lastUpdateTick, double currentValue, double rota)
        {
            this.lastUpdateTick = lastUpdateTick;
            this.currentValue = currentValue;
            this.rota = rota;
        }

        public Delta() {}

        private void update(ClientLevel level, TemperatureLevel temperatureLevel)
        {
            if (level.getGameTime() == this.lastUpdateTick)
                return;

            this.lastUpdateTick = level.getGameTime();
            double targetValue = temperatureLevel.ordinal() * 0.25;
            double delta = targetValue - this.currentValue;

            // Add a small increment to the rota to move towards the target value
            this.rota += delta * 0.1;

            // Diminish the rota over time. The clock uses 0.9, but we want slightly less wobbling
            this.rota *= 0.87;
            this.currentValue = Mth.clamp(this.currentValue + this.rota, 0.0, 1.0);
        }

        public float getValue()
        {
            // Round to the nearest 0.05
            return (float)((double)Math.round(this.currentValue * 20.0) / 20.0);
        }

        private long lastUpdateTick()
        {
            return this.lastUpdateTick;
        }

        private double currentValue()
        {
            return this.currentValue;
        }

        private double rota()
        {
            return this.rota;
        }
    }
}

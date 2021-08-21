/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.temperature.IPositionalTemperatureModifier;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.api.thirst.IThirst;
import toughasnails.config.ServerConfig;
import toughasnails.core.ToughAsNails;

import java.util.List;

public class TemperatureHelperImpl implements TemperatureHelper.Impl.ITemperatureHelper
{
    protected static final EntityDataAccessor<Integer> DATA_TICKS_HYPERTHERMIC = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    // Positional:
    // TODO: Offset by nearby heat sources
    // TODO: Offset by season

    private static List<IPositionalTemperatureModifier> positionalModifiers = Lists.newArrayList(TemperatureHelperImpl::nightModifier);

    @Override
    public TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos)
    {
        Biome biome = level.getBiome(pos);
        TemperatureLevel temperature = getBiomeTemperatureLevel(biome, pos);

        for (IPositionalTemperatureModifier modifier : positionalModifiers)
        {
            temperature = modifier.modify(level, pos, temperature);
        }

        return temperature;
    }

    private static ITemperature lastTemperature;

    @Override
    public ITemperature getPlayerTemperature(Player player)
    {
        ITemperature temperature = player.getCapability(TANCapabilities.TEMPERATURE).orElse(lastTemperature);
        lastTemperature = temperature;
        return temperature;
    }

    @Override
    public boolean isTemperatureEnabled()
    {
        return ServerConfig.enableTemperature.get();
    }

    @Override
    public void setTicksHyperthermic(Player player, int ticks)
    {
        player.getEntityData().set(DATA_TICKS_HYPERTHERMIC, ticks);
    }

    @Override
    public float getPercentHyperthermic(Player player)
    {
        int i = getTicksRequiredForHyperthermia();
        return (float)Math.min(getTicksHyperthermic(player), i) / (float)i;
    }

    @Override
    public boolean isFullyHyperthermic(Player player)
    {
        return getTicksHyperthermic(player) >= getTicksRequiredForHyperthermia();
    }

    @Override
    public int getTicksRequiredForHyperthermia()
    {
        return 140;
    }

    @Override
    public int getTicksHyperthermic(Player player)
    {
        return player.getEntityData().get(DATA_TICKS_HYPERTHERMIC);
    }

    private static TemperatureLevel getBiomeTemperatureLevel(Biome biome, BlockPos pos)
    {
        float biomeTemperature = biome.getTemperature(pos);

        if (biomeTemperature < 0.15F) return TemperatureLevel.ICY;
        else if (biomeTemperature >= 0.15F && biomeTemperature < 0.5F) return TemperatureLevel.COLD;
        else if (biomeTemperature >= 0.5F && biomeTemperature < 0.7F) return TemperatureLevel.NEUTRAL;
        else if (biomeTemperature >= 0.7F && biomeTemperature < 0.9F) return TemperatureLevel.WARM;
        else if (biomeTemperature > 0.9F) return TemperatureLevel.HOT;

        return TemperatureLevel.NEUTRAL;
    }

    private static TemperatureLevel nightModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        // Drop the temperature during the night
        if (level.isNight())
        {
            if (current == TemperatureLevel.HOT)
                current = current.decrement(2);
            else if (current != TemperatureLevel.NEUTRAL)
                current = current.decrement(1);
        }

        return current;
    }
}

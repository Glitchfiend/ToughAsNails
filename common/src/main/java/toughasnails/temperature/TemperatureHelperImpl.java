/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import toughasnails.api.enchantment.TANEnchantments;
import toughasnails.api.player.ITANPlayer;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.*;
import toughasnails.api.temperature.IProximityBlockModifier.Type;
import toughasnails.block.entity.ThermoregulatorBlockEntity;
import toughasnails.init.ModConfig;
import toughasnails.init.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TemperatureHelperImpl implements TemperatureHelper.Impl.ITemperatureHelper
{
    protected static List<IPositionalTemperatureModifier> positionalModifiers = Lists.newArrayList(TemperatureHelperImpl::altitudeModifier, TemperatureHelperImpl::rainModifier);
    protected static List<IProximityBlockModifier> proximityModifiers = new ArrayList<>();
    protected static List<IPlayerTemperatureModifier> playerModifiers = Lists.newArrayList(TemperatureHelperImpl::thermoregulatorModifier, TemperatureHelperImpl::immersionModifier);

    @Override
    public TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos)
    {
        TemperatureLevel temperature = getBiomeTemperatureLevel(level, pos);

        for (IPositionalTemperatureModifier modifier : positionalModifiers)
        {
            temperature = modifier.modify(level, pos, temperature);
        }

        // Manually do the night and proximity modifiers as they must be after any others
        temperature = nightModifier(level, pos, temperature);
        return proximityModifier(level, pos, temperature);
    }

    public static TemperatureLevel getTemperatureAtPosWithoutProximity(Level level, BlockPos pos)
    {
        TemperatureLevel temperature = getBiomeTemperatureLevel(level, pos);

        for (IPositionalTemperatureModifier modifier : positionalModifiers)
        {
            temperature = modifier.modify(level, pos, temperature);
        }

        return nightModifier(level, pos, temperature);
    }

    @Override
    public ITemperature getPlayerTemperature(Player player)
    {
        return ((ITANPlayer)player).getTemperatureData();
    }

    @Override
    public boolean isTemperatureEnabled()
    {
        return ModConfig.temperature.enableTemperature;
    }

    @Override
    public void setTicksHyperthermic(Player player, int ticks)
    {
        this.getPlayerTemperature(player).setHyperthermiaTicks(ticks);
    }

    @Override
    public float getPercentHyperthermic(Player player)
    {
        int i = this.getTicksRequiredForHyperthermia();
        return (float)Math.min(getTicksHyperthermic(player), i) / (float)i;
    }

    @Override
    public boolean isFullyHyperthermic(Player player)
    {
        return this.getTicksHyperthermic(player) >= this.getTicksRequiredForHyperthermia();
    }

    @Override
    public int getTicksRequiredForHyperthermia()
    {
        return 140;
    }

    @Override
    public int getTicksHyperthermic(Player player)
    {
        return this.getPlayerTemperature(player).getHyperthermiaTicks();
    }

    @Override
    public boolean isHeating(BlockState state)
    {
        return state.is(ModTags.Blocks.HEATING_BLOCKS) && (!state.hasProperty(CampfireBlock.LIT) || state.getValue(CampfireBlock.LIT) || !state.hasProperty(CopperBulbBlock.POWERED) || state.getValue(CopperBulbBlock.POWERED));
    }

    @Override
    public boolean isCooling(BlockState state)
    {
        return state.is(ModTags.Blocks.COOLING_BLOCKS) && (!state.hasProperty(CampfireBlock.LIT) || state.getValue(CampfireBlock.LIT) || !state.hasProperty(CopperBulbBlock.POWERED) || state.getValue(CopperBulbBlock.POWERED));
    }

    @Override
    public void registerPlayerTemperatureModifier(IPlayerTemperatureModifier modifier)
    {
        playerModifiers.add(modifier);
    }

    @Override
    public void registerPositionalTemperatureModifier(IPositionalTemperatureModifier modifier)
    {
        positionalModifiers.add(modifier);
    }

    @Override
    public void registerProximityBlockModifier(IProximityBlockModifier modifier)
    {
        proximityModifiers.add(modifier);
    }

    private static TemperatureLevel getBiomeTemperatureLevel(Level level, BlockPos pos)
    {
        Holder<Biome> biome = level.getBiome(pos);
        float biomeTemperature = biome.value().getBaseTemperature();

        if (!level.dimensionType().natural() || (pos.getY() > ModConfig.temperature.environmentalModifierAltitude || pos.getY() >= level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).below().getY()))
        {
            if (biome.is(ModTags.Biomes.ICY_BIOMES)) return TemperatureLevel.ICY;
            else if (biome.is(ModTags.Biomes.COLD_BIOMES)) return TemperatureLevel.COLD;
            else if (biome.is(ModTags.Biomes.NEUTRAL_BIOMES)) return TemperatureLevel.NEUTRAL;
            else if (biome.is(ModTags.Biomes.WARM_BIOMES)) return TemperatureLevel.WARM;
            else if (biome.is(ModTags.Biomes.HOT_BIOMES)) return TemperatureLevel.HOT;
            else if (biomeTemperature < 0.15F) return TemperatureLevel.ICY;
            else if (biomeTemperature >= 0.15F && biomeTemperature < 0.45F) return TemperatureLevel.COLD;
            else if (biomeTemperature >= 0.45F && biomeTemperature < 0.85F) return TemperatureLevel.NEUTRAL;
            else if (biomeTemperature >= 0.85F && biomeTemperature < 1.0F) return TemperatureLevel.WARM;
            else if (biomeTemperature >= 1.0F) return TemperatureLevel.HOT;
        }

        return TemperatureLevel.NEUTRAL;
    }

    private static TemperatureLevel altitudeModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        if (pos.getY() > ModConfig.temperature.temperatureDropAltitude) current = current.decrement(1);
        else if (pos.getY() < ModConfig.temperature.temperatureRiseAltitude) current = current.increment(1);
        return current;
    }

    private static TemperatureLevel rainModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        Holder<Biome> biome = level.getBiome(pos);

        if (isExposedToRain(level, pos))
        {
            if (coldEnoughToSnow(level, biome, pos))
                current = current.increment(ModConfig.temperature.snowTemperatureChange);
            else
                current = current.increment(ModConfig.temperature.wetTemperatureChange);
        }

        return current;
    }

    private static TemperatureLevel nightModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        // level.isNight is unavailable on the client, so we roughly approximate with level.getTimeOfDay()
        float time = level.getTimeOfDay(1.0F);
        boolean isNight = time >= 0.25F && time <= 0.75F;

        // Drop the temperature during the night
        if (level.dimensionType().natural() && isNight && (pos.getY() > ModConfig.temperature.environmentalModifierAltitude || pos.getY() >= level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).below().getY()))
        {
            if (current == TemperatureLevel.HOT)
                current = current.increment(ModConfig.temperature.nightHotTemperatureChange);
            else if (current != TemperatureLevel.NEUTRAL)
                current = current.increment(ModConfig.temperature.nightTemperatureChange);
        }

        return current;
    }

    private static TemperatureLevel proximityModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        Set<BlockPos> heating = Sets.newHashSet();
        Set<BlockPos> cooling = Sets.newHashSet();

        // If the player's position is obstructed (for example, when mounted or inside a block), use the position above instead
        if (!level.isEmptyBlock(pos))
            pos = pos.above();

        AreaFill.fill(level, pos, (checkerLevel, checkedPos) -> {
           addHeatingOrCooling(heating, cooling, checkerLevel, checkedPos.pos());
        });

        if (heating.size() > cooling.size()) current = current.increment(1);
        else if (cooling.size() > heating.size()) current = current.decrement(1);
        return current;
    }

    private static void addHeatingOrCooling(Set<BlockPos> heating, Set<BlockPos> cooling, Level level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);

        if (TemperatureHelper.isHeatingBlock(state))
        {
            heating.add(pos);
        }
        else if (TemperatureHelper.isCoolingBlock(state))
        {
            cooling.add(pos);
        }
        else
        {
            for(IProximityBlockModifier modifier : proximityModifiers)
            {
                Type sourceType = modifier.getProximityType(level, pos, state);

                if(sourceType == Type.HEATING)
                {
                    heating.add(pos);
                }
                else if(sourceType == Type.COOLING)
                {
                    cooling.add(pos);
                }
            }
        }
    }

    private static TemperatureLevel thermoregulatorModifier(Player player, TemperatureLevel current)
    {
        return modifyTemperatureByThermoregulators(player.level(), TemperatureHelper.getTemperatureData(player).getNearbyThermoregulators(), player.blockPosition(), current);
    }

    private static TemperatureLevel immersionModifier(Player player, TemperatureLevel current)
    {
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        ITemperature temperature = TemperatureHelper.getTemperatureData(player);

        if (player.isOnFire()) current = current.increment(ModConfig.temperature.onFireTemperatureChange);
        if (player.isInPowderSnow) current = current.increment(ModConfig.temperature.powderSnowTemperatureChange);
        if (isExposedToRain(level, pos))
        {
            // Only set the dry ticks here, as the temperature change associated with rain has already been handled by the positional modifier
            temperature.setDryTicks(0);
        }
        else if (!(player.getRootVehicle() instanceof Boat) && (player.isInWater() || level.getFluidState(pos).is(FluidTags.WATER)))
        {
            temperature.setDryTicks(0);
            current = current.increment(ModConfig.temperature.wetTemperatureChange);
        }
        else
        {
            if (temperature.getDryTicks() < ModConfig.temperature.wetTicks)
                current.increment(ModConfig.temperature.wetTemperatureChange);
        }

        return current;
    }

    protected static TemperatureLevel handheldModifier(Player player, TemperatureLevel current)
    {
        AtomicInteger coolingItems = new AtomicInteger();
        AtomicInteger heatingItems = new AtomicInteger();

        player.getHandSlots().forEach((stack -> {
            if (stack.is(ModTags.Items.COOLING_HELD_ITEMS)) coolingItems.getAndIncrement();
            if (stack.is(ModTags.Items.HEATING_HELD_ITEMS)) heatingItems.getAndIncrement();
        }));

        return current.increment(heatingItems.get() - coolingItems.get());
    }

    protected static TemperatureLevel armorModifier(Player player, TemperatureLevel current)
    {
        AtomicInteger coolingPieces = new AtomicInteger();
        AtomicInteger heatingPieces = new AtomicInteger();

        player.getArmorSlots().forEach((stack -> {
            // Prevent doubling of effects if armor were to be tagged as heating/cooling and also had a heating/cooling trim applied
            if (stack.is(ModTags.Items.COOLING_ARMOR) || stack.is(ModTags.Items.HEATING_ARMOR))
            {
                if (stack.is(ModTags.Items.COOLING_ARMOR)) coolingPieces.getAndIncrement();
                if (stack.is(ModTags.Items.HEATING_ARMOR)) heatingPieces.getAndIncrement();
            }
            else
            {
                ArmorTrim.getTrim(player.level().registryAccess(), stack, true).ifPresent(armorTrim -> {
                    Holder<TrimMaterial> trimMaterial = armorTrim.material();
                    if (trimMaterial.is(ModTags.Trims.COOLING_TRIMS)) coolingPieces.getAndIncrement();
                    if (trimMaterial.is(ModTags.Trims.HEATING_TRIMS)) heatingPieces.getAndIncrement();
                });
            }
        }));

        // Prevent armor from sending players over the edge into hot or icy temperature levels
        TemperatureLevel armorAdjTemp = current.increment(heatingPieces.get() / 2 - coolingPieces.get() / 2);
        if (armorAdjTemp == TemperatureLevel.HOT && current != TemperatureLevel.HOT) current = armorAdjTemp.decrement(1);
        else if (armorAdjTemp == TemperatureLevel.ICY && current != TemperatureLevel.ICY) current = armorAdjTemp.increment(1);
        else current = armorAdjTemp;

        // Armor enchantments
        if (EnchantmentHelper.getEnchantmentLevel(TANEnchantments.THERMAL_TUNING, player) > 0)
            current = TemperatureLevel.NEUTRAL;

        return current;
    }

    protected static TemperatureLevel internalModifier(Player player, TemperatureLevel current)
    {
        TemperatureLevel newTemperature = current;

        if (player.hasEffect(TANEffects.INTERNAL_WARMTH)) newTemperature = newTemperature.increment(1);
        if (player.hasEffect(TANEffects.INTERNAL_CHILL)) newTemperature = newTemperature.decrement(1);

        // Prevent internal effects from sending players into extremities
        if (newTemperature == TemperatureLevel.HOT && current != TemperatureLevel.HOT) current = newTemperature.decrement(1);
        else if (newTemperature == TemperatureLevel.ICY && current != TemperatureLevel.ICY) current = newTemperature.increment(1);
        else current = newTemperature;

        return current;
    }

    private static boolean isExposedToRain(Level level, BlockPos pos)
    {
        return level.isRaining() && pos.getY() >= level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).below().getY();
    }

    private static boolean coldEnoughToSnow(Level level, Holder<Biome> biome, BlockPos pos)
    {
        return biome.value().coldEnoughToSnow(pos);
    }

    public static TemperatureLevel modifyTemperatureByThermoregulators(Level level, Set<BlockPos> thermoregulators, BlockPos checkPos, TemperatureLevel current)
    {
        int coolingCount = 0;
        int heatingCount = 0;
        int neutralCount = 0;

        for (BlockPos pos : thermoregulators)
        {
            ThermoregulatorBlockEntity blockEntity = (ThermoregulatorBlockEntity)level.getBlockEntity(pos);

            if (blockEntity == null)
                continue;

            switch (blockEntity.getEffectAtPos(checkPos))
            {
                case COOLING -> ++coolingCount;
                case HEATING -> ++heatingCount;
                case NEUTRALIZING -> ++neutralCount;
            }
        }

        if (coolingCount == 0 && heatingCount == 0 && neutralCount > 0) return TemperatureLevel.NEUTRAL;
        else if (coolingCount > heatingCount) return current.decrement(2);
        else if (heatingCount > coolingCount) return current.increment(2);
        else return current;
    }
}

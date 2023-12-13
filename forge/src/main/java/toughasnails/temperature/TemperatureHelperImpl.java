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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import sereneseasons.season.SeasonHooks;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.enchantment.TANEnchantments;
import toughasnails.api.temperature.*;
import toughasnails.api.temperature.IProximityBlockModifier.Type;
import toughasnails.config.ServerConfig;
import toughasnails.config.TemperatureConfig;
import toughasnails.init.ModTags;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TemperatureHelperImpl implements TemperatureHelper.Impl.ITemperatureHelper
{
    protected static List<IPositionalTemperatureModifier> positionalModifiers = Lists.newArrayList(TemperatureHelperImpl::altitudeModifier);
    protected static List<IProximityBlockModifier> proximityModifiers = new ArrayList<>();
    protected static List<IPlayerTemperatureModifier> playerModifiers = Lists.newArrayList(TemperatureHelperImpl::immersionModifier);

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

        if (pos.getY() > TemperatureConfig.environmentalModifierAltitude.get() || level.canSeeSky(pos))
        {
            if (biome.is(ModTags.Biomes.ICY_BIOMES)) return TemperatureLevel.ICY;
            else if (biome.is(ModTags.Biomes.COLD_BIOMES)) return TemperatureLevel.COLD;
            else if (biome.is(ModTags.Biomes.NEUTRAL_BIOMES)) return TemperatureLevel.NEUTRAL;
            else if (biome.is(ModTags.Biomes.WARM_BIOMES)) return TemperatureLevel.WARM;
            else if (biome.is(ModTags.Biomes.HOT_BIOMES)) return TemperatureLevel.HOT;
            else if (biomeTemperature < 0.15F) return TemperatureLevel.ICY;
            else if (biomeTemperature >= 0.15F && biomeTemperature < 0.45F) return TemperatureLevel.COLD;
            else if (biomeTemperature >= 0.45F && biomeTemperature < 0.75F) return TemperatureLevel.NEUTRAL;
            else if (biomeTemperature >= 0.75F && biomeTemperature < 0.9F) return TemperatureLevel.WARM;
            else if (biomeTemperature >= 0.9F) return TemperatureLevel.HOT;
        }

        return TemperatureLevel.NEUTRAL;
    }

    private static TemperatureLevel altitudeModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        if (pos.getY() > TemperatureConfig.temperatureDropAltitude.get()) current = current.decrement(1);
        else if (pos.getY() < TemperatureConfig.temperatureRiseAltitude.get()) current = current.increment(1);
        return current;
    }

    private static TemperatureLevel nightModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        // Drop the temperature during the night
        if (level.isNight() && (pos.getY() > TemperatureConfig.environmentalModifierAltitude.get() || level.canSeeSky(pos)))
        {
            if (current == TemperatureLevel.HOT)
                current = current.increment(TemperatureConfig.nightHotTemperatureChange.get());
            else if (current != TemperatureLevel.NEUTRAL)
                current = current.increment(TemperatureConfig.nightTemperatureChange.get());
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

        fill(heating, cooling, level, pos);

        if (heating.size() > cooling.size()) current = current.increment(1);
        else if (cooling.size() > heating.size()) current = current.decrement(1);
        return current;
    }

    private static final int PROXIMITY_RADIUS = TemperatureConfig.nearHeatCoolProximity.get();

    private static void fill(Set<BlockPos> heating, Set<BlockPos> cooling, Level level, BlockPos pos)
    {
        Set<BlockPos> checked = Sets.newHashSet();
        Queue<BlockPos> queue = new LinkedList();

        int minX = pos.getX() - PROXIMITY_RADIUS;
        int maxX = pos.getX() + PROXIMITY_RADIUS;

        queue.add(pos);
        while (!queue.isEmpty())
        {
            BlockPos posToCheck = queue.poll();

            // Skip already checked positions
            if (checked.contains(posToCheck))
                continue;

            // Positive x is east, negative x is west
            if (level.isEmptyBlock(posToCheck))
            {
                BlockPos westPos = posToCheck;
                BlockPos eastPos = posToCheck.east();

                while (level.isEmptyBlock(westPos) && westPos.getX() >= minX)
                {
                    checked.add(westPos);
                    expand(queue, checked, heating, cooling, level, pos, westPos);
                    westPos = westPos.west();
                }

                while (level.isEmptyBlock(eastPos) && eastPos.getX() <= maxX)
                {
                    checked.add(eastPos);
                    expand(queue, checked, heating, cooling, level, pos, eastPos);
                    eastPos = eastPos.east();
                }

                // Add the first non-air blocks (or nothing if still air)
                if (level.isEmptyBlock(westPos)) checked.add(westPos);
                else addHeatingOrCooling(checked, heating, cooling, level, westPos);

                if (level.isEmptyBlock(eastPos)) checked.add(eastPos);
                else addHeatingOrCooling(checked, heating, cooling, level, eastPos);
            }
            else
            {
                addHeatingOrCooling(checked, heating, cooling, level, posToCheck);
            }
        }
    }

    private static void expand(Queue<BlockPos> queue, Set<BlockPos> checked, Set<BlockPos> heating, Set<BlockPos> cooling, Level level, BlockPos origin, BlockPos pos)
    {
        BlockPos north = pos.north(); // Negative Z
        BlockPos south = pos.south(); // Positive Z
        BlockPos down = pos.below(); // Negative Y
        BlockPos up = pos.above(); // Positive Y

        int minZ = origin.getZ() - PROXIMITY_RADIUS;
        int maxZ = origin.getZ() + PROXIMITY_RADIUS;
        int minY = origin.getY() - PROXIMITY_RADIUS;
        int maxY = origin.getY() + PROXIMITY_RADIUS;

        if (north.getZ() >= minZ)
        {
            if (level.isEmptyBlock(north)) queue.add(north);
            else addHeatingOrCooling(checked, heating, cooling, level, north);
        }

        if (south.getZ() <= maxZ)
        {
            if (level.isEmptyBlock(south)) queue.add(south);
            else addHeatingOrCooling(checked, heating, cooling, level, south);
        }

        if (down.getY() >= minY)
        {
            if (level.isEmptyBlock(down)) queue.add(down);
            else addHeatingOrCooling(checked, heating, cooling, level, down);
        }

        if (up.getY() <= maxY)
        {
            if (level.isEmptyBlock(up)) queue.add(up);
            else addHeatingOrCooling(checked, heating, cooling, level, up);
        }
    }

    private static void addHeatingOrCooling(Set<BlockPos> checked, Set<BlockPos> heating, Set<BlockPos> cooling, Level level, BlockPos pos)
    {
        checked.add(pos);
        BlockState state = level.getBlockState(pos);

        if (state.is(ModTags.Blocks.HEATING_BLOCKS) && (!state.hasProperty(CampfireBlock.LIT) || state.getValue(CampfireBlock.LIT)))
        {
            heating.add(pos);
        }
        else if (state.is(ModTags.Blocks.COOLING_BLOCKS))
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

    private static TemperatureLevel immersionModifier(Player player, TemperatureLevel current)
    {
        Level level = player.level();
        BlockPos pos = player.blockPosition();

        if (player.isOnFire()) current = current.increment(TemperatureConfig.onFireTemperatureChange.get());
        if (player.isInPowderSnow) current = current.increment(TemperatureConfig.powderSnowTemperatureChange.get());
        if (player.level().isRaining() && player.level().canSeeSky(pos)) {
            player.getCapability(TANCapabilities.TEMPERATURE).ifPresent(cap -> cap.setDryTicks(0));
            Holder<Biome> biome = player.level().getBiome(pos);

            if (ModList.get().isLoaded("sereneseasons"))
            {
                if (!SeasonHooks.warmEnoughToRainSeasonal(player.level(), biome, pos))
                    current = current.increment(TemperatureConfig.snowTemperatureChange.get());
                else
                    current.increment(TemperatureConfig.wetTemperatureChange.get());
            }
            else
            {
                if (biome.value().coldEnoughToSnow(pos))
                    current = current.increment(TemperatureConfig.snowTemperatureChange.get());
                else
                    current.increment(TemperatureConfig.wetTemperatureChange.get());
            }
        }
        else if(!(player.getRootVehicle() instanceof Boat) && (player.isInWater() || level.getFluidState(pos).is(FluidTags.WATER)))
        {
            player.getCapability(TANCapabilities.TEMPERATURE).ifPresent(cap -> cap.setDryTicks(0));
            current = current.increment(TemperatureConfig.wetTemperatureChange.get());
        }
        else
        {
            // Looks scuffed but should actually be safe, cap is always attached to player.
            ITemperature cap = player.getCapability(TANCapabilities.TEMPERATURE).orElse(null);

            if(cap != null && cap.getDryTicks() < TemperatureConfig.wetTicks.get())
                current.increment(TemperatureConfig.wetTemperatureChange.get());
        }

        return current;
    }

    protected static TemperatureLevel handheldModifier(Player player, TemperatureLevel current)
    {
        AtomicInteger coolingItems = new AtomicInteger();
        AtomicInteger heatingItems = new AtomicInteger();

        player.getHandSlots().forEach((stack -> {
            if (stack.is(ModTags.Items.COOLING_ITEMS)) coolingItems.getAndIncrement();
            if (stack.is(ModTags.Items.HEATING_ITEMS)) heatingItems.getAndIncrement();
        }));

        return current.increment(heatingItems.get() - coolingItems.get());
    }

    protected static TemperatureLevel armorModifier(Player player, TemperatureLevel current)
    {
        AtomicInteger coolingPieces = new AtomicInteger();
        AtomicInteger heatingPieces = new AtomicInteger();

        player.getArmorSlots().forEach((stack -> {
            if (stack.is(ModTags.Items.COOLING_ARMOR)) coolingPieces.getAndIncrement();
            if (stack.is(ModTags.Items.HEATING_ARMOR)) heatingPieces.getAndIncrement();
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
}

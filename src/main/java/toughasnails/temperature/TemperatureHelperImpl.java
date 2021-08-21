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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.enchantment.TANEnchantments;
import toughasnails.api.temperature.*;
import toughasnails.api.thirst.IThirst;
import toughasnails.config.ServerConfig;
import toughasnails.config.TemperatureConfig;
import toughasnails.core.ToughAsNails;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TemperatureHelperImpl implements TemperatureHelper.Impl.ITemperatureHelper
{
    protected static List<IPositionalTemperatureModifier> positionalModifiers = Lists.newArrayList(TemperatureHelperImpl::nightModifier);
    protected static List<IPlayerTemperatureModifier> playerModifiers = Lists.newArrayList(TemperatureHelperImpl::immersionModifier);

    @Override
    public TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos)
    {
        Biome biome = level.getBiome(pos);
        TemperatureLevel temperature = getBiomeTemperatureLevel(biome, pos);

        for (IPositionalTemperatureModifier modifier : positionalModifiers)
        {
            temperature = modifier.modify(level, pos, temperature);
        }

        // Manually do the proximity modifier to ensure it is always last
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
        this.getPlayerTemperature(player).setTicksHyperthermic(ticks);
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
        return this.getPlayerTemperature(player).getTicksHyperthermic();
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
                current = current.increment(TemperatureConfig.nightWarmTemperatureChange.get());
            else if (current != TemperatureLevel.NEUTRAL)
                current = current.increment(TemperatureConfig.nightCoolTemperatureChange.get());
        }

        return current;
    }

    private static final int PROXIMITY_RADIUS = 7;
    private static final double CLOSE_THRESHOLD = 3*3;

    private static TemperatureLevel proximityModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        int numCloseCoolSources = 0;
        int numFarCoolSources = 0;
        int numCloseHeatSources = 0;
        int numFarHeatSources = 0;

        for (int x = -PROXIMITY_RADIUS; x <= PROXIMITY_RADIUS; x++)
        {
            for (int y = -PROXIMITY_RADIUS; y <= PROXIMITY_RADIUS; y++)
            {
                for (int z = -PROXIMITY_RADIUS; z <= PROXIMITY_RADIUS; z++)
                {
                    BlockPos newPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(newPos);
                    boolean isClose = newPos.distSqr(pos) <= CLOSE_THRESHOLD;

                    if (state.getMaterial() == Material.FIRE || state.getMaterial() == Material.LAVA || TemperatureConfig.isWarmingBlock(state.getBlock()))
                    {
                        if (isClose) numCloseHeatSources++;
                        else numFarHeatSources++;
                    }
                    else if (TemperatureConfig.isCoolingBlock(state.getBlock()))
                    {
                        if (isClose) numCloseCoolSources++;
                        else numFarCoolSources++;
                    }
                }
            }
        }

        int sum = numCloseHeatSources * 2 + numFarHeatSources - (numCloseCoolSources * 2 + numFarCoolSources);
        if (sum > 0) current = current.increment(sum > 1 ? 2 : 1);
        else if (sum < 0) current = current.decrement(sum < 1 ? 2 : 1);
        return current;
    }

    private static TemperatureLevel immersionModifier(Player player, TemperatureLevel current)
    {
        if (player.isOnFire()) current = current.increment(TemperatureConfig.onFireTemperatureChange.get());
        if (player.isInPowderSnow) current = current.increment(TemperatureConfig.powderSnowTemperatureChange.get());
        if (player.isInWaterOrRain()) current = current.increment(TemperatureConfig.wetTemperatureChange.get());
        return current;
    }

    protected static TemperatureLevel armorModifier(Player player, TemperatureLevel current)
    {
        AtomicInteger coolingPieces = new AtomicInteger();
        AtomicInteger heatingPieces = new AtomicInteger();

        player.getArmorSlots().forEach((stack -> {
            if (TemperatureConfig.isCoolingArmor(stack.getItem())) coolingPieces.getAndIncrement();
            if (TemperatureConfig.isWarmingArmor(stack.getItem())) heatingPieces.getAndIncrement();
        }));

        current = current.increment(heatingPieces.get() / 2 - coolingPieces.get() / 2);

        // Armor enchantments
        if (EnchantmentHelper.getEnchantmentLevel(TANEnchantments.TEMPERATURE_TUNING, player) > 0)
            current = TemperatureLevel.NEUTRAL;

        return current;
    }
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import sereneseasons.season.SeasonHooks;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.enchantment.TANEnchantments;
import toughasnails.api.temperature.*;
import toughasnails.config.ServerConfig;
import toughasnails.config.TemperatureConfig;
import toughasnails.init.ModTags;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TemperatureHelperImpl implements TemperatureHelper.Impl.ITemperatureHelper
{
    protected static List<IPositionalTemperatureModifier> positionalModifiers = Lists.newArrayList(TemperatureHelperImpl::altitudeModifier, TemperatureHelperImpl::nightModifier);
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

    private static TemperatureLevel getBiomeTemperatureLevel(Biome biome, BlockPos pos)
    {
        float biomeTemperature = biome.getBaseTemperature();

        if (pos.getY() > TemperatureConfig.environmentalModifierAltitude.get())
        {
            if (biomeTemperature < 0.15F) return TemperatureLevel.ICY;
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
        if (level.isNight() && pos.getY() > TemperatureConfig.environmentalModifierAltitude.get())
        {
            if (current == TemperatureLevel.HOT)
                current = current.increment(TemperatureConfig.nightHotTemperatureChange.get());
            else if (current != TemperatureLevel.NEUTRAL)
                current = current.increment(TemperatureConfig.nightTemperatureChange.get());
        }

        return current;
    }

    private static final int PROXIMITY_RADIUS = 7;

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
                    boolean isClose = newPos.distSqr(pos) <= (Math.pow(TemperatureConfig.nearBlockRange.get(), 2) + 1.0D);

                    if (state.is(ModTags.Blocks.HEATING_BLOCKS))
                    {
                        if (isClose) numCloseHeatSources++;
                        else numFarHeatSources++;
                    }
                    else if (state.is(ModTags.Blocks.COOLING_BLOCKS))
                    {
                        if (isClose) numCloseCoolSources++;
                        else numFarCoolSources++;
                    }
                }
            }
        }

        int closeSum = numCloseHeatSources - numCloseCoolSources;
        int farSum = numFarHeatSources - numFarCoolSources;

        if (closeSum > 0) current = current.increment(2);
        else if (closeSum < 0) current = current.decrement(2);
        else if (farSum > 0) current = current.increment(1);
        else if (farSum < 0) current = current.decrement(1);
        
        return current;
    }

    private static TemperatureLevel immersionModifier(Player player, TemperatureLevel current)
    {
        if (player.isOnFire()) current = current.increment(TemperatureConfig.onFireTemperatureChange.get());
        if (player.isInPowderSnow) current = current.increment(TemperatureConfig.powderSnowTemperatureChange.get());
        if (player.isInWaterOrRain()) current = current.increment(TemperatureConfig.wetTemperatureChange.get());
        if (player.level.isRaining() && player.level.canSeeSky(player.blockPosition()))
        {
            Biome biome = player.level.getBiome(player.blockPosition());

            if (ModList.get().isLoaded("sereneseasons"))
            {
                if (SeasonHooks.isColdEnoughToSnowHook(biome, player.blockPosition(), player.level))
                    current = current.increment(TemperatureConfig.snowTemperatureChange.get());
            }
            else if (biome.isColdEnoughToSnow(player.blockPosition()))
                current = current.increment(TemperatureConfig.snowTemperatureChange.get());
        }

        return current;
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

        // Armor enchantments
        if (EnchantmentHelper.getEnchantmentLevel(TANEnchantments.THERMAL_TUNING, player) > 0)
            current = TemperatureLevel.NEUTRAL;

        return current;
    }
}

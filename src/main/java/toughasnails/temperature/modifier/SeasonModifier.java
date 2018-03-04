/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.temperature.modifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;
import toughasnails.util.TerrainUtils;

public class SeasonModifier extends TemperatureModifier
{
    public SeasonModifier(String id)
    {
        super(id);
    }

    @Override
    public Temperature applyEnvironmentModifiers(World world, BlockPos pos, Temperature initialTemperature, IModifierMonitor monitor)
    {
        int temperatureLevel = initialTemperature.getRawValue();
        SubSeason season = SeasonHelper.getSeasonData(world).getSubSeason();

        if (!(SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS)))
        {
            season = SubSeason.MID_SUMMER;
        }

        if (world.provider.isSurfaceWorld())
        {
            int temperatureModifier = 0;
            switch (season)
            {
                case EARLY_SPRING:
                    temperatureModifier = ModConfig.temperature.earlySpringModifier;
                    break;
    
                case MID_SPRING:
                    temperatureModifier = ModConfig.temperature.midSpringModifier;
                    break;
    
                case LATE_SPRING:
                    temperatureModifier = ModConfig.temperature.lateSpringModifier;
                    break;
    
                case EARLY_SUMMER:
                    temperatureModifier = ModConfig.temperature.earlySummerModifier;
                    break;
    
                case MID_SUMMER:
                    temperatureModifier = ModConfig.temperature.midSummerModifier;
                    break;
    
                case LATE_SUMMER:
                    temperatureModifier = ModConfig.temperature.lateSummerModifier;
                    break;
    
                case EARLY_AUTUMN:
                    temperatureModifier = ModConfig.temperature.earlyAutumnModifier;
                    break;
    
                case MID_AUTUMN:
                    temperatureModifier = ModConfig.temperature.midAutumnModifier;
                    break;
    
                case LATE_AUTUMN:
                    temperatureModifier = ModConfig.temperature.lateAutumnModifier;
                    break;
    
                case EARLY_WINTER:
                    temperatureModifier = ModConfig.temperature.earlyWinterModifier;
                    break;
    
                case MID_WINTER:
                    temperatureModifier = ModConfig.temperature.midWinterModifier;
                    break;
    
                case LATE_WINTER:
                    temperatureModifier = ModConfig.temperature.lateWinterModifier;
                    break;
    
                default:
                    break;
            }
            
            // Apply underground equilibrium
            temperatureModifier = Math.round(TerrainUtils.getAverageUndergroundCoefficient(world, pos) * temperatureModifier);
            temperatureLevel += temperatureModifier;
        }
        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Season", initialTemperature, new Temperature(temperatureLevel)));

        return new Temperature(temperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return false;
    }

}

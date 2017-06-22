/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.config.GameplayOption;
import toughasnails.init.ModConfig;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;

public class SeasonModifier extends TemperatureModifier
{
    public SeasonModifier(TemperatureDebugger debugger) 
    {
        super(debugger);
    }

    @Override
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature) 
    {
        int temperatureLevel = temperature.getRawValue();
        SubSeason season = SeasonHelper.getSeasonData(world).getSubSeason();
        
        if (!(SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS)))
        {
        	season = SubSeason.MID_SUMMER;
        }
        
        debugger.start(Modifier.SEASON_TARGET, temperatureLevel);
        
        if (world.provider.isSurfaceWorld())
        {
	        switch (season)
	        {
	        case EARLY_SPRING:
	            temperatureLevel += ModConfig.temperature.earlySpringModifier;
                break;

			case MID_SPRING:
				temperatureLevel += ModConfig.temperature.midSpringModifier;
				break;
                
	        case LATE_SPRING:
	            temperatureLevel += ModConfig.temperature.lateSpringModifier;
                break;
                
	        case EARLY_SUMMER:
	            temperatureLevel += ModConfig.temperature.earlySummerModifier;
	            break;
	            
	        case MID_SUMMER:
	            temperatureLevel += ModConfig.temperature.midSummerModifier;
	            break;
	            
	        case LATE_SUMMER:
	            temperatureLevel += ModConfig.temperature.lateSummerModifier;
	            break;
	            
	        case EARLY_AUTUMN:
	            temperatureLevel += ModConfig.temperature.earlyAutumnModifier;
	            break;

			case MID_AUTUMN:
				temperatureLevel += ModConfig.temperature.midAutumnModifier;
				break;
	            
	        case LATE_AUTUMN:
	            temperatureLevel += ModConfig.temperature.lateAutumnModifier;
	            break;
	            
	        case EARLY_WINTER:
	            temperatureLevel += ModConfig.temperature.earlyWinterModifier;
	            break;
	            
	        case MID_WINTER:
	            temperatureLevel += ModConfig.temperature.midWinterModifier;
	            break;
	            
	        case LATE_WINTER:
                temperatureLevel += ModConfig.temperature.lateWinterModifier;
                break;
	            
	        default:
	            break;
	        }
        }
        debugger.end(temperatureLevel);
        
        return new Temperature(temperatureLevel);
    }

}

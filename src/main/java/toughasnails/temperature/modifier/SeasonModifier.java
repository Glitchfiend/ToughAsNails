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
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.config.GameplayOption;
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
        
        if (!(SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS)))
        {
        	season = SubSeason.MID_SUMMER;
        }
        
        debugger.start(Modifier.SEASON_TARGET, temperatureLevel);
        
        if (world.provider.isSurfaceWorld())
        {
	        switch (season)
	        {
	        case MID_WINTER: case LATE_WINTER:
	            temperatureLevel -= 6;
	            break;
	        
	        case EARLY_SPRING: case EARLY_WINTER:
	            temperatureLevel -= 4;
	            break;
	            
	        case MID_SPRING: case LATE_AUTUMN:
	            temperatureLevel -= 2;
	            break;
	            
	        case MID_SUMMER: case EARLY_AUTUMN:
	            temperatureLevel += 2;
	            break;
	            
	        case LATE_SUMMER:
	            temperatureLevel += 4;
	            break;
	            
	        default:
	            break;
	        }
        }
        debugger.end(temperatureLevel);
        
        return new Temperature(temperatureLevel);
    }

}

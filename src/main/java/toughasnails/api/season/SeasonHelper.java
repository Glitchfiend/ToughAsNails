/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

import net.minecraft.world.World;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.config.GameplayOption;

public class SeasonHelper 
{
    /** 
     * Obtains data about the state of the season cycle in the world. This works both on
     * the client and the server.
     */
    public static ISeasonData getSeasonData(World world)
    {
        ISeasonData data;
        
        try
        {
            if (!world.isRemote)
            {
                data = (ISeasonData)Class.forName("toughasnails.handler.season.SeasonHandler").getMethod("getServerSeasonData", World.class).invoke(null, world);
            }
            else
            {
                data = (ISeasonData)Class.forName("toughasnails.handler.season.SeasonHandler").getMethod("getClientSeasonData").invoke(null);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("An error occurred obtaining season data", e);
        }

        return data;
    }
    
    /**
     * Checks if the season provided allows snow to fall at a certain
     * biome temperature.
     * 
     * @param season The season to check
     * @param temperature The biome temperature to check
     * @return True if suitable, otherwise false
     */
    public static boolean canSnowAtTempInSeason(Season season, float temperature)
    {
        //If we're in winter, the temperature can be anything equal to or below 0.7
        return temperature < 0.15F || (season == Season.WINTER && temperature <= 0.7F && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS));
    }
}

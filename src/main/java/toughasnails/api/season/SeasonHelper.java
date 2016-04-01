/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

import net.minecraft.world.World;

public class SeasonHelper 
{
    /** 
     * Obtains data about the state of the season cycle in the world. This will only work
     * server-side.
     */
    public static ISeasonData getSeasonData(World world)
    {
        ISeasonData data;
        
        try
        {
            data = (ISeasonData)Class.forName("toughasnails.handler.SeasonHandler").getMethod("getSeasonData", World.class).invoke(null, world);
        }
        catch (Exception e)
        {
            throw new RuntimeException("An error occurred obtaining season data", e);
        }
        
        return data;
    }
}

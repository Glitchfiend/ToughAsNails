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
     * Obtains data about the state of the season cycle in the world. This works both on
     * the client and the server
     */
    public static ISeasonData getSeasonData(World world)
    {
        ISeasonData data;
        
        try
        {
            if (!world.isRemote)
            {
                data = (ISeasonData)Class.forName("toughasnails.handler.SeasonHandler").getMethod("getServerSeasonData", World.class).invoke(null, world);
            }
            else
            {
                data = (ISeasonData)Class.forName("toughasnails.handler.SeasonHandler").getMethod("getClientSeasonData").invoke(null);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("An error occurred obtaining season data", e);
        }

        return data;
    }
}

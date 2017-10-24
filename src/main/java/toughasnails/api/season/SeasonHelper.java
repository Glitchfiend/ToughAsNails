/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;

public class SeasonHelper
{
    public static ISeasonDataProvider dataProvider;

    /**
     * Obtains data about the state of the season cycle in the world. This works
     * both on the client and the server.
     */
    public static ISeasonData getSeasonData(World world)
    {
        ISeasonData data;

        if (!world.isRemote)
        {
            data = dataProvider.getServerSeasonData(world);
        }
        else
        {
            data = dataProvider.getClientSeasonData();
        }

        return data;
    }

    /**
     * Checks if the season provided allows snow to fall at a certain biome
     * temperature.
     * 
     * @param season
     *            The season to check
     * @param temperature
     *            The biome temperature to check
     * @return True if suitable, otherwise false
     */
    public static boolean canSnowAtTempInSeason(Season season, float temperature)
    {
        // If we're in winter, the temperature can be anything equal to or below
        // 0.7
        return temperature < 0.15F || (season == Season.WINTER && temperature <= 0.7F && SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS));
    }

    public interface ISeasonDataProvider
    {
        ISeasonData getServerSeasonData(World world);

        ISeasonData getClientSeasonData();
    }

    public static float getSeasonFloatTemperature(Biome biome, BlockPos pos, Season season)
    {
        if (biome.getTemperature() <= 0.7F && season == Season.WINTER && SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS))
        {
            return 0.0F;
        }
        else
        {
            return biome.getFloatTemperature(pos);
        }
    }
}

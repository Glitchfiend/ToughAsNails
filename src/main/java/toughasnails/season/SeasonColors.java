/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import toughasnails.api.season.Season.SubSeason;

public class SeasonColors 
{
    private static int[][] grassColorBuffer = new int[SubSeason.values().length][65536];
    private static int[][] foliageColorBuffer = new int[SubSeason.values().length][65536];
    
    public static void setGrassColorForSeason(SubSeason season, int[] grassBufferIn)
    {
        grassColorBuffer[season.ordinal()] = grassBufferIn;
    }
    
    public static void setFoliageColorForSeason(SubSeason season, int[] foliageColor)
    {
        foliageColorBuffer[season.ordinal()] = foliageColor;
    }
    
    public static int getGrassColorForSeason(SubSeason season, double temperature, double humidity)
    {
        humidity = humidity * temperature;
        int i = (int)((1.0D - temperature) * 255.0D);
        int j = (int)((1.0D - humidity) * 255.0D);
        int k = j << 8 | i;
        return k > grassColorBuffer[season.ordinal()].length ? -65281 : grassColorBuffer[season.ordinal()][k];
    }
    
    public static int getFoliageColorForSeason(SubSeason season, double temperature, double humidity)
    {
        humidity = humidity * temperature;
        int i = (int)((1.0D - temperature) * 255.0D);
        int j = (int)((1.0D - humidity) * 255.0D);
        return foliageColorBuffer[season.ordinal()][j << 8 | i];
    }
}

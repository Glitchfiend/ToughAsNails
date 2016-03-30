/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import net.minecraft.world.World;
import toughasnails.api.season.Season;
import toughasnails.api.season.Season.SubSeason;

public final class Calendar 
{
    /** Not configurable, the duration of a single day*/
    private static final int DAY_TICKS = /*24000*/20;
    /** The duration of a sub season in days*/
    public static final int SUB_SEASON_DURATION = 5;
    public static final int TOTAL_CYCLE_TICKS = (DAY_TICKS * SUB_SEASON_DURATION) * SubSeason.values().length;
    
    public final int time;
    
    public Calendar(int time)
    {
        this.time = time;
    }
    
    public int getDay()
    {
        return (int)(this.time / DAY_TICKS);
    }
    
    public SubSeason getSubSeason()
    {
        int index = (getDay() / SUB_SEASON_DURATION) % SubSeason.values().length;
        return SubSeason.values()[index];
    }
    
    public Season getSeason()
    {
        return this.getSubSeason().getSeason();
    }
}

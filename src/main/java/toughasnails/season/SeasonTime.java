/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import toughasnails.api.season.ISeasonData;
import toughasnails.api.season.Season;
import toughasnails.api.season.Season.SubSeason;

public final class SeasonTime implements ISeasonData
{
    /** Not configurable, the duration of a single day*/
    public static final int DAY_TICKS = 24000;
    /** The duration of a sub season in days*/
    public static final int SUB_SEASON_DURATION = 5;
    public static final int SEASON_TICKS = DAY_TICKS * SUB_SEASON_DURATION * 3;
    public static final int TOTAL_CYCLE_TICKS = (DAY_TICKS * SUB_SEASON_DURATION) * SubSeason.values().length;
    
    public final int time;
    
    public SeasonTime(int time)
    {
        this.time = time;
    }
    
    public int getDay()
    {
        return (int)(this.time / DAY_TICKS);
    }
    
    @Override
    public int getSeasonCycleTicks() 
    {
        return this.time;
    }
    
    @Override
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

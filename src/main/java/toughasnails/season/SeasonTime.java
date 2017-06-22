/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import com.google.common.base.Preconditions;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.ISeasonData;
import toughasnails.api.season.Season;
import toughasnails.api.season.Season.SubSeason;

public final class SeasonTime implements ISeasonData
{
    public static final SeasonTime ZERO = new SeasonTime(0);
    public final int time;
    
    public SeasonTime(int time)
    {
        Preconditions.checkArgument(time >= 0, "Time cannot be negative!");
        this.time = time;
    }

    @Override
    public int getDayDuration()
    {
        return SyncedConfig.getIntValue(SeasonsOption.DAY_DURATION);
    }

    @Override
    public int getSubSeasonDuration()
    {
        return getDayDuration() * SyncedConfig.getIntValue(SeasonsOption.SUB_SEASON_DURATION);
    }

    @Override
    public int getSeasonDuration()
    {
        return getSubSeasonDuration() * 3;
    }

    @Override
    public int getCycleDuration()
    {
        return getSubSeasonDuration() * SubSeason.values().length;
    }
    
    @Override
    public int getSeasonCycleTicks() 
    {
        return this.time;
    }

    @Override
    public int getDay()
    {
        return this.time / getDayDuration();
    }

    @Override
    public SubSeason getSubSeason()
    {
        int index = (this.time / getSubSeasonDuration()) % SubSeason.values().length;
        return SubSeason.values()[index];
    }

    @Override
    public Season getSeason()
    {
        return this.getSubSeason().getSeason();
    }
}

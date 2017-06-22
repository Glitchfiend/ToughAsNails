/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

import toughasnails.api.season.Season.SubSeason;

public interface ISeasonData 
{
    /**
     * Get the duration of a single day. Normally this is
     * 24000 ticks.
     *
     * @return The duration in ticks
     */
    int getDayDuration();

    /**
     * Get the duration of a single sub season.
     *
     * @return The duration in ticks
     */
    int getSubSeasonDuration();

    /**
     * Get the duration of a single season.
     *
     * @return The duration in ticks
     */
    int getSeasonDuration();

    /**
     * Get the duration of an entire cycle (a 'year')
     *
     * @return The duration in ticks
     */
    int getCycleDuration();

    /**
     * The time elapsed in ticks for the current overall cycle.
     * A cycle can be considered equivalent to a year, and is comprised
     * of Summer, Autumn, Winter and Spring.
     *
     * @return The time in ticks
     */
    int getSeasonCycleTicks();

    /**
     * Get the number of days elapsed.
     *
     * @return The current day
     */
    int getDay();

    /**
     * Get the current sub season.
     *
     * @return The current sub season
     */
    SubSeason getSubSeason();

    /**
     * Get the current season. This method is
     * mainly for convenience.
     *
     * @return The current season
     */
    Season getSeason();
}

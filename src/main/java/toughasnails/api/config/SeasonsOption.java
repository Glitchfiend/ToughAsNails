/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.config;

public enum SeasonsOption implements ISyncedOption
{
    ENABLE_SEASONS("Enable Seasons"), DAY_DURATION("Day Duration"), SUB_SEASON_DURATION("Sub Season Duration"), NUM_PATCHES_PER_TICK("Chunk patches per tick"), PATCH_TICK_DISTANCE("Ticks between patches");

    private final String optionName;

    SeasonsOption(String name)
    {
        this.optionName = name;
    }

    @Override
    public String getOptionName()
    {
        return this.optionName;
    }
}

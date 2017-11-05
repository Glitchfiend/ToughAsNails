/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import toughasnails.api.config.SeasonsOption;
import toughasnails.core.ToughAsNails;

import java.io.File;

public class SeasonsConfig extends ConfigHandler
{
    public static final String TIME_SETTINGS = "Time Settings";
    public static final String EVENT_SETTINGS = "Event Settings";
    public static final String AESTHETIC_SETTINGS = "Aesthetic Settings";
    public static final String PERFORMANCE_SETTINGS = "Performance Settings";

    public boolean winterCropDeath;
    public boolean changeGrassColour;
    public boolean changeFoliageColour;

    public SeasonsConfig(File configFile)
    {
        super(configFile);
    }

    @Override
    protected void loadConfiguration()
    {
        try
        {
            addSyncedValue(SeasonsOption.ENABLE_SEASONS, true, "Toggle", "Seasons progress as days increase");
            addSyncedValue(SeasonsOption.DAY_DURATION, 24000, TIME_SETTINGS, "The duration of a Minecraft day in ticks", 20, Integer.MAX_VALUE);
            addSyncedValue(SeasonsOption.SUB_SEASON_DURATION, 5, TIME_SETTINGS, "The duration of a sub season in days", 1, Integer.MAX_VALUE);
            addSyncedValue(SeasonsOption.NUM_PATCHES_PER_TICK, 20, PERFORMANCE_SETTINGS, "The amount of chunk patches per server tick. Lower number increases server performance, but increases popping artifacts.", 1, Integer.MAX_VALUE);
            addSyncedValue(SeasonsOption.PATCH_TICK_DISTANCE, 20 * 5, PERFORMANCE_SETTINGS, "The amount of ticks to keep between patching a chunk.", 0, Integer.MAX_VALUE);

            // Only applicable server-side
            winterCropDeath = config.getBoolean("Enable Winter Crop Death", EVENT_SETTINGS, true, "Kill unheated crops during the winter");

            // Client-only. The server shouldn't get to decide these.
            changeGrassColour = config.getBoolean("Change Grass Colour Seasonally", AESTHETIC_SETTINGS, true, "Change the grass colour based on the current season");
            changeFoliageColour = config.getBoolean("Change Foliage Colour Seasonally", AESTHETIC_SETTINGS, true, "Change the foliage colour based on the current season");
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Tough As Nails has encountered a problem loading seasons.cfg", e);
        }
        finally
        {
            if (config.hasChanged())
                config.save();
        }
    }
}

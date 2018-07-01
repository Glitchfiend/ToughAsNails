/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import java.io.File;

import toughasnails.api.config.GameplayOption;
import toughasnails.core.ToughAsNails;

public class GameplayConfig extends ConfigHandler
{
    public boolean iceCubeDrops;
    public boolean magmaShardDrops;
    public boolean lootTableTweaks;

    public GameplayConfig(File configFile)
    {
        super(configFile, "Gameplay Settings");
    }

    @Override
    protected void loadConfiguration()
    {
        try
        {
        	addSyncedValue(GameplayOption.ENABLE_TEMPERATURE, true, "Main Settings", "Players are affected by temperature");
            addSyncedValue(GameplayOption.ENABLE_THIRST, true, "Main Settings", "Players are affected by thirst.");
            addSyncedValue(GameplayOption.ENABLE_PEACEFUL, false, "Main Settings", "The effects of the mod will work on Peaceful difficulty.");
            addSyncedValue(GameplayOption.ENABLE_THIRST_WORLD, true, "Main Settings", "Allows drinking directly from water-source blocks with sneak-rightclick and empty main hand");
            addSyncedValue(GameplayOption.ENABLE_THIRST_RAIN, false, "Main Settings", "Allows drinking from rain by sneak-rightclick and empty main hand if looking up at raining sky.");

            iceCubeDrops = config.getBoolean("Ice Cube Drops", "Tweak Settings", true, "Ice Blocks drop Ice Cubes.");
            magmaShardDrops = config.getBoolean("Magma Shard Drops", "Tweak Settings", true, "Magma Blocks drop Magma Shards.");
            lootTableTweaks = config.getBoolean("Loot Table Tweaks", "Tweak Settings", true, "Loot tables are tweaked for balance.");
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Tough As Nails has encountered a problem loading gameplay.cfg", e);
        }
        finally
        {
            if (config.hasChanged()) config.save();
        }
    }
}

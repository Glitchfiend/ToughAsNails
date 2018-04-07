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
    public static final String SURVIVAL_SETTINGS = "Survival Settings";

    public boolean iceCubeDrops;

    public GameplayConfig(File configFile)
    {
        super(configFile, "Gameplay Settings");
    }

    @Override
    protected void loadConfiguration()
    {
        try
        {
            addSyncedValue(GameplayOption.ENABLE_THIRST, true, SURVIVAL_SETTINGS, "Players are affected by thirst.");

            iceCubeDrops = config.getBoolean("Ice Cube Drops", SURVIVAL_SETTINGS, true, "Ice Blocks drop Ice Cubes.");
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

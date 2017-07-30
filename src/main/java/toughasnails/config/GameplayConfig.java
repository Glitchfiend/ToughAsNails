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
    public static final String STARTING_HEALTH = "Staring health";

    public GameplayConfig(File configFile)
    {
        super(configFile);
    }

    @Override
    protected void loadConfiguration()
    {
        try
        {
            addSyncedValue(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH, true, SURVIVAL_SETTINGS, "Players begin with a lowered maximum health.");
            addSyncedValue(GameplayOption.ENABLE_THIRST, true, SURVIVAL_SETTINGS, "Players are affected by thirst");
            addSyncedValue(GameplayOption.EASY_STARTING_HEARTS, 7, STARTING_HEALTH, "Amount of hearts players will start with on easy", 1, 10);
            addSyncedValue(GameplayOption.NORMAL_STARTING_HEARTS, 5, STARTING_HEALTH, "Amount of hearts players will start with on normal", 1, 10);
            addSyncedValue(GameplayOption.HARD_STARTING_HEARTS, 3, STARTING_HEALTH, "Amount of hearts players will start with on hard", 1, 10);
            addSyncedValue(GameplayOption.MAX_HEARTS, 10, SURVIVAL_SETTINGS, "Max amount of hearts players can get", 10, 100);
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

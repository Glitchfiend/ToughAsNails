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

    public int easyStartingHearts;
    public int normalStartingHearts;
    public int hardStartingHearts;
    public int maxHearts;

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

            easyStartingHearts = config.getInt("Easy starting hearts", STARTING_HEALTH, 7, 1, 10, "Amount of hearts players will start with on easy");
            normalStartingHearts = config.getInt("Normal starting hearts", STARTING_HEALTH, 5, 1, 10, "Amount of hearts players will start with on normal");
            hardStartingHearts = config.getInt("Hard starting hearts", STARTING_HEALTH, 3, 1, 10, "Amount of hearts players will start with on hard");
            maxHearts = config.getInt("Max hearts", SURVIVAL_SETTINGS, 10, 10, 100, "Max amount of hearts players can get");
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

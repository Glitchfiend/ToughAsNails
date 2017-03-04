/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.core.ToughAsNails;

public class GameplayConfigurationHandler
{
    public static final String SURVIVAL_SETTINGS = "Survival Settings";
    
    public static Configuration config;
    
    public static void init(File configFile)
    {
        if (config == null)
        {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {
        try
        {
            addSyncedBool(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH, true, SURVIVAL_SETTINGS, "Players begin with a lowered maximum health.");
            addSyncedBool(GameplayOption.ENABLE_SEASONS, true, SURVIVAL_SETTINGS, "Seasons progress as days increase");
            addSyncedBool(GameplayOption.ENABLE_TEMPERATURE, true, SURVIVAL_SETTINGS, "Players are affected by temperature");
            addSyncedBool(GameplayOption.ENABLE_THIRST, true, SURVIVAL_SETTINGS, "Players are affected by thirst");
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
    
    private static void addSyncedBool(GameplayOption option, boolean defaultValue, String category, String comment)
    {
        boolean value = config.getBoolean(option.getOptionName(), category, defaultValue, comment);
        SyncedConfig.addOption(option, "" + value);
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(ToughAsNails.MOD_ID))
        {
            loadConfiguration();
        }
    }
}

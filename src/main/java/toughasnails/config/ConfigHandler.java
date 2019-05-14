/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.ISyncedOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;

import java.io.File;
import java.util.Set;

public abstract class ConfigHandler
{
    protected final Configuration config;
    public final String description;

    protected ConfigHandler(File configFile, String description)
    {
        config = new Configuration(configFile);
        loadConfiguration();

        MinecraftForge.EVENT_BUS.register(this);
        this.description = description;
        ModConfig.configHandlers.add(this);
    }

    public Set<String> getCategoryNames()
    {
    	return config.getCategoryNames();
    }
    
    public ConfigCategory getCategory(String categoryName)
    {
    	return config.getCategory(categoryName);
    }
    
    protected abstract void loadConfiguration();

    protected <T> void addSyncedValue(ISyncedOption option, T defaultValue, String category, String comment)
    {
        String value = "";

        if (defaultValue instanceof String)
        {
            value = config.getString(option.getOptionName(), category, defaultValue.toString(), comment);
        }
        else if (defaultValue instanceof Boolean)
        {
            value = "" + config.getBoolean(option.getOptionName(), category, (Boolean)defaultValue, comment);
        }

        SyncedConfig.addOption(option, value);
    }

    protected <T> void addSyncedValue(ISyncedOption option, T defaultValue, String category, String comment, T minValue, T maxValue)
    {   	
        String value = "";

        if (defaultValue instanceof Integer)
        {
            value = "" + config.getInt(option.getOptionName(), category, (Integer)defaultValue, (Integer)minValue, (Integer)maxValue, comment);
        }
        else if (defaultValue instanceof Float)
        {
            value = "" + config.getFloat(option.getOptionName(), category, (Float)defaultValue, (Float)minValue, (Float)maxValue, comment);
        }

        SyncedConfig.addOption(option, value);
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

/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.temperature.BlockTemperatureData;
import toughasnails.temperature.BlockTemperatureStateData;
import toughasnails.temperature.MaterialTemperatureData;

public class GameplayConfigurationHandler
{
    public static final String SURVIVAL_SETTINGS = "Survival Settings";
    
    public static Configuration config;
    
    public static HashMap<String, BlockTemperatureData> blockTemperatureData;
    
    public static MaterialTemperatureData materialTemperatureData;
    
    public static void init(File configFile)
    {
        if (config == null)
        {
            config = new Configuration(configFile);
            loadConfiguration();
            
            //Block temperature config:
            blockTemperatureData = new HashMap<String, BlockTemperatureData>();
            
            String blockConfigName = FilenameUtils.getFullPath(configFile.getPath()) + "blockTemperatureConfig.json";
            
            try
            {
            	File temperatureConfigFile = new File(blockConfigName);
            	
            	//No config file, so create default config:
            	if (!temperatureConfigFile.exists())
            	{
            		Gson serializer = new GsonBuilder().setPrettyPrinting().create();
            		
            		BlockTemperatureData[] defaultTemperatureDataArray = {
            			new BlockTemperatureData("toughasnails:campfire", 0.0F, new BlockTemperatureStateData[] {new BlockTemperatureStateData("burning", "true", 12F)}),
            			new BlockTemperatureData("minecraft:lit_furnace", 12.0F, new BlockTemperatureStateData[0]),
            			new BlockTemperatureData("minecraft:lava", 1.5F, new BlockTemperatureStateData[0]),
            			new BlockTemperatureData("minecraft:flowing_lava", 1.5F, new BlockTemperatureStateData[0]),
            		};
            		
            		String defaultTempJsonString = serializer.toJson(defaultTemperatureDataArray);
            		
            		FileUtils.writeStringToFile(temperatureConfigFile, defaultTempJsonString);
            	}
        		
            	String jsonString = FileUtils.readFileToString(temperatureConfigFile);
            	
        		try
        		{
        			Gson gson = new Gson();
        			
        			BlockTemperatureData[] tempDataArray = gson.fromJson(jsonString, BlockTemperatureData[].class);
        			
        			for (BlockTemperatureData tempDataObj : tempDataArray)
        			{
        				//TODO: Clever merging, or just rely on user to not duplicate?
        				blockTemperatureData.put(tempDataObj.blockName, tempDataObj);
        			}
        			
        		}
        		catch (Exception e)
        		{
        			ToughAsNails.logger.error("Error parsing block temperature config from json: " + blockConfigName, e);
        		}
				
			}
            catch (Exception e)
            {
            	ToughAsNails.logger.error("Error parsing block temperature config: " + blockConfigName, e);
			}
            
            
            //Material temperature config:
            materialTemperatureData = new MaterialTemperatureData();
            
            String materialConfigName = FilenameUtils.getFullPath(configFile.getPath()) + "materialTemperatureConfig.json";
            
            try
            {
            	File temperatureConfigFile = new File(materialConfigName);
            	
            	if (!temperatureConfigFile.exists())
            	{
            		Gson serializer = new GsonBuilder().setPrettyPrinting().create();
            		
            		String defaultTempJsonString = serializer.toJson(materialTemperatureData);
            		
            		FileUtils.writeStringToFile(temperatureConfigFile, defaultTempJsonString);
            	}
            	
            	String jsonString = FileUtils.readFileToString(temperatureConfigFile);
            	
            	try
            	{
            		Gson gson = new Gson();
            		
            		materialTemperatureData = gson.fromJson(jsonString, MaterialTemperatureData.class);
            	}
            	catch (Exception e)
            	{
            		ToughAsNails.logger.error("Error parsing material temperature config from json: " + materialConfigName, e);
            	}
            	
            }
            catch (Exception e)
            {
            	ToughAsNails.logger.error("Error parsing material temperature config: " + materialConfigName, e);
			}
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

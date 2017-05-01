/*******************************************************************************
 * Copyright 2014-2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package toughasnails.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import toughasnails.api.TANBlocks;
import toughasnails.block.BlockTANCampfire;
import toughasnails.core.ToughAsNails;
import toughasnails.temperature.BlockTemperatureData;
import toughasnails.temperature.MaterialTemperatureData;
import toughasnails.util.BlockStateUtils;

public class TANConfig
{

    public static Gson serializer = new GsonBuilder().setPrettyPrinting().create();
    public static JsonParser parser = new JsonParser();

    public static HashMap<String, ArrayList<BlockTemperatureData>> blockTemperatureData;

    public static MaterialTemperatureData materialTemperatureData;

    public static void init(File configDir)
    {

        //Block temperature config:
        blockTemperatureData = new HashMap<String, ArrayList<BlockTemperatureData>>();

        File blockTemperatureConfigFile = new File(configDir, "block_temperature.json");

        //No config file, so create default config:
        if (!blockTemperatureConfigFile.exists())
        {
            try
            {
                BlockTemperatureData[] defaultBlockTemperatureData = {
                        new BlockTemperatureData(TANBlocks.campfire.getDefaultState().withProperty(BlockTANCampfire.BURNING, true), new String[]{BlockTANCampfire.BURNING.getName()}, 12.0F),
                        new BlockTemperatureData(Blocks.LIT_FURNACE.getDefaultState(), new String[0], 12.0F),
                        new BlockTemperatureData(Blocks.LAVA.getDefaultState(), new String[0], 1.5F),
                        new BlockTemperatureData(Blocks.FLOWING_LAVA.getDefaultState(), new String[0], 1.5F),
                };

                //Need to do this manually as there is some issue with getting Gson to serialise an IBlockState directly due to duplicated keys
                JsonArray tempAry = new JsonArray();

                for(BlockTemperatureData tempData : defaultBlockTemperatureData)
                {
                    tempAry.add(asJsonObject(tempData));
                }

                writeFile(blockTemperatureConfigFile, tempAry);
            }
            catch (Exception e)
            {
                ToughAsNails.logger.error("Error creating default block temperature config file: " + blockTemperatureConfigFile.toString(), e);
            }
        }


        try
        {
            String blockJsonString = FileUtils.readFileToString(blockTemperatureConfigFile);

            JsonElement blockAry = parser.parse(blockJsonString);

            if (blockAry == null)
            {
                ToughAsNails.logger.error("Error parsing block temperature config from json file: " + blockTemperatureConfigFile.toString() + " temperature information array does not exist.");
            }

            if (blockAry.isJsonArray())
            {
                for (JsonElement ele : blockAry.getAsJsonArray())
                {
                    BlockTemperatureData tempData = asBlockTemperatureData(ele, "Error parsing block temperature state configuration " + ele.toString());

                    String blockName = tempData.state.getBlock().getRegistryName().toString();

                    if (!blockTemperatureData.containsKey(blockName))
                    {
                        blockTemperatureData.put(blockName, new ArrayList<BlockTemperatureData>());
                    }

                    blockTemperatureData.get(blockName).add(tempData);
                }
            }
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Error parsing block temperature config from json: " + blockTemperatureConfigFile.toString(), e);
        }


        //Material temperature config:
        materialTemperatureData = new MaterialTemperatureData();

        File materialTemperatureConfigFile = new File(configDir, "material_temperature.json");

        try
        {
            if (!materialTemperatureConfigFile.exists())
            {
                writeFile(materialTemperatureConfigFile, materialTemperatureData);
            }            
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Error creating default material temperature config file: " + materialTemperatureConfigFile.toString(), e);
        }    

        try
        {
            String materialJsonString = FileUtils.readFileToString(materialTemperatureConfigFile);

            Gson gson = new Gson();

            materialTemperatureData = gson.fromJson(materialJsonString, MaterialTemperatureData.class);
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Error parsing material temperature config from json: " + materialTemperatureConfigFile.toString(), e);
        }

    }


    protected static boolean writeFile(File outputFile, Object obj)
    {
        try
        {
            FileUtils.write(outputFile, serializer.toJson(obj));
            return true;
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Error writing config file " + outputFile.getAbsolutePath() + ": " + e.getMessage());
            return false;
        }
    }


    protected static Map<String,JsonElement> parse(String jsonString)
    {
        Map<String,JsonElement> members;

        members = new HashMap<String, JsonElement>();
        if (jsonString == null) {return members;}

        JsonElement rootElement = null;
        try
        {
            rootElement = parser.parse(jsonString);
            if (rootElement != null)
            {
                if (rootElement.isJsonObject())
                {
                    for (Entry<String, JsonElement> entry : rootElement.getAsJsonObject().entrySet())
                    {
                        members.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    ToughAsNails.logger.error("Error parsing config: not a JSON object");
                }
            }
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Error parsing config: "+e.getMessage());
        }

        return members;
    }

    protected static IBlockState asBlockState(JsonElement ele, String extraPrefix)
    {

        try {

            JsonObject obj = ele.getAsJsonObject();

            // attempt to load the specified block
            if (!obj.has("block"))
            {
                ToughAsNails.logger.error(extraPrefix + " Block name missing");
                return null;
            }
            JsonElement blockName = obj.get("block");
            if (!blockName.isJsonPrimitive())
            {
                ToughAsNails.logger.error(extraPrefix + " Invalid block name - must be a string");
                return null;
            }
            Block block = Block.getBlockFromName(blockName.getAsString());
            if (block == null)
            {
                ToughAsNails.logger.error(extraPrefix + " Unrecognised block name " + blockName.getAsString());
                return null;
            }

            IBlockState state = block.getDefaultState();

            // attempt to add properties
            if (obj.has("properties"))
            {
                JsonElement properties = obj.get("properties");
                if (!properties.isJsonObject())
                {
                    ToughAsNails.logger.error(extraPrefix + " Invalid properties list - must be a JSON object");
                    return state;
                }

                for (Entry<String, JsonElement> entry : properties.getAsJsonObject().entrySet())
                {
                    IProperty property = BlockStateUtils.getPropertyByName(state, entry.getKey());
                    if (property != null)
                    {
                        Comparable propertyValue = BlockStateUtils.getPropertyValueByName(state, property, entry.getValue().getAsString());
                        if (propertyValue != null)
                        {
                            state = state.withProperty(property, propertyValue);
                        }
                        else
                        {
                            ToughAsNails.logger.error(extraPrefix + " Invalid value " + entry.getValue().getAsString() + " for property " + entry.getKey());
                        }
                    }
                    else
                    {
                        ToughAsNails.logger.error(extraPrefix + " Invalid property name: " + entry.getKey());
                    }
                }
            }

            return state;

        }
        catch (Exception e)
        {
            ToughAsNails.logger.error(extraPrefix + " Error fetching blockstate: " + e.getMessage());
            return null;
        }

    }

    protected static JsonObject asJsonObject(IBlockState state)
    {
        try
        {
            JsonObject obj = new JsonObject();

            obj.addProperty("block", state.getBlock().getRegistryName().toString());


            JsonObject props = new JsonObject();

            for (IProperty<?> blockProperty : state.getProperties().keySet())
            {
                props.addProperty(blockProperty.getName(), state.getValue(blockProperty).toString());
            }

            obj.add("properties", props);

            return obj;
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Error converting blockstate to Json: " + e.getMessage());
            return null;
        }
    }

    protected static JsonObject asJsonObject(BlockTemperatureData blockTemperatureData)
    {
        JsonObject blockTempJson = new JsonObject();

        blockTempJson.add("state", asJsonObject(blockTemperatureData.state));
        blockTempJson.add("use_properties", serializer.toJsonTree(blockTemperatureData.use_properties));
        blockTempJson.addProperty("temperature", blockTemperatureData.blockTemperature);

        return blockTempJson;
    }

    protected static BlockTemperatureData asBlockTemperatureData(JsonElement ele, String extraPrefix)
    {
        try
        {
            JsonObject obj = ele.getAsJsonObject();

            // attempt to load the state
            if (!obj.has("state"))
            {
                ToughAsNails.logger.error(extraPrefix + " Block state missing");
                return null;
            }
            JsonElement blockState = obj.get("state");
            if (!blockState.isJsonObject())
            {
                ToughAsNails.logger.error(extraPrefix + " Invalid block state - must be an object");
                return null;
            }

            IBlockState state = asBlockState(blockState, extraPrefix);

            // attempt to get properties to use for comparison
            if (!obj.has("use_properties"))
            {
                ToughAsNails.logger.error(extraPrefix + " Block use properties missing");
                return null;
            }

            JsonElement blockUse_properties = obj.get("use_properties");
            if (!blockUse_properties.isJsonArray())
            {
                ToughAsNails.logger.error(extraPrefix + " Invalid use properties - must be an array");
                return null;
            }

            Gson gson = new Gson();

            String[] use_properties = gson.fromJson(blockUse_properties, String[].class); 

            //Check specified property names are valid:
            for(String propertyName : use_properties)
            {
                if(!BlockStateUtils.isValidPropertyName(state, propertyName))
                {
                    ToughAsNails.logger.error(extraPrefix + " Invalid use property name: " + propertyName + ", valid property names are " + state.getPropertyKeys().toString());
                    return null;
                }
            }


            // attempt to get the temperature value
            if(!obj.has("temperature"))
            {
                ToughAsNails.logger.error(extraPrefix + " block temperature missing");
                return null;
            }
            JsonElement blockTemperature = obj.get("temperature");
            if(!blockTemperature.isJsonPrimitive())
            {
                ToughAsNails.logger.error(extraPrefix + " Invalid block temperature - must be a float");
            }

            float temperature = blockTemperature.getAsFloat();

            return new BlockTemperatureData(state, use_properties, temperature);

        }
        catch (Exception e)
        {
            ToughAsNails.logger.error(extraPrefix + " Error fetching block temperature data: " + e.getMessage());
            return null;
        }

    }

}
/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import toughasnails.api.TANBlocks;
import toughasnails.block.BlockTANCampfire;
import toughasnails.config.ConfigHandler;
import toughasnails.config.GameplayConfig;
import toughasnails.config.SeasonsConfig;
import toughasnails.config.TemperatureConfig;
import toughasnails.config.json.*;
import toughasnails.util.config.JsonUtil;

public class ModConfig
{
    public static List<ConfigHandler> configHandlers = Lists.newArrayList();

    public static Map<String, List<BlockTemperatureData>> blockTemperatureData = Maps.newHashMap();
    public static Map<String, List<DrinkData>> drinkData = Maps.newHashMap();
    public static MaterialTemperatureData materialTemperatureData;

    public static GameplayConfig gameplay;
    public static SeasonsConfig seasons;
    public static TemperatureConfig temperature;

    public static void preInit(File configDir)
    {
        gameplay = new GameplayConfig(new File(configDir, "gameplay.cfg"));
        seasons = new SeasonsConfig(new File(configDir, "seasons.cfg"));
        temperature = new TemperatureConfig(new File(configDir, "temperature.cfg"));
    }

    public static void init(File configDir)
    {
        //Block json config:
        List<BlockTemperatureData> defaultBlockTemperatureData = Lists.newArrayList(
                new BlockTemperatureData(new BlockStatePredicate(TANBlocks.campfire.getDefaultState().withProperty(BlockTANCampfire.BURNING, true), Sets.newHashSet(BlockTANCampfire.BURNING)), 5.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.LIT_FURNACE), 3.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.LAVA), 15.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.FLOWING_LAVA), 15.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.MAGMA), 12.5F));

        if (ModCompat.HOT_SPRING_WATER != null) defaultBlockTemperatureData.add(new BlockTemperatureData(new BlockStatePredicate(ModCompat.HOT_SPRING_WATER), 5.0F));

        List<BlockTemperatureData> blockTempDataList = JsonUtil.getOrCreateConfigFile(configDir, "block_temperature.json", defaultBlockTemperatureData, new TypeToken<List<BlockTemperatureData>>(){}.getType());
        populateDataMap(blockTempDataList, blockTemperatureData, tempData -> tempData.predicate.getBlock().getRegistryName().toString());

        //Material json config:
        materialTemperatureData = JsonUtil.getOrCreateConfigFile(configDir, "material_temperature.json", new MaterialTemperatureData(), new TypeToken<MaterialTemperatureData>(){}.getType());

        // Drink json config
        List<DrinkData> defaultDrinkData = Lists.newArrayList(
                new DrinkData(new ItemPredicate(Items.MILK_BUCKET, 0), 6, 0.7F, 0.0F));
        List<DrinkData> drinkDataList = JsonUtil.getOrCreateConfigFile(configDir, "drink_stats.json", defaultDrinkData, new TypeToken<List<DrinkData>>(){}.getType());
        populateDataMap(drinkDataList, drinkData, drinkData -> drinkData.getPredicate().getItemStack().getItem().getRegistryName().toString());
    }

    private static <T> void populateDataMap(List<T> dataList, Map<String, List<T>> dataMap, Function<T, String> keyGetter)
    {
        for (T data : dataList)
        {
            String key = keyGetter.apply(data);

            if (!dataMap.containsKey(key))
            {
                dataMap.put(key, new ArrayList<>());
            }

            dataMap.get(key).add(data);
        }
    }
}

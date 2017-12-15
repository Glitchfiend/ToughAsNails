/*******************************************************************************
 * Copyright 2014-2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package toughasnails.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import net.minecraft.init.Blocks;
import toughasnails.api.TANBlocks;
import toughasnails.block.BlockTANCampfire;
import toughasnails.config.temperature.BlockTemperatureData;
import toughasnails.config.temperature.MaterialTemperatureData;
import toughasnails.init.ModCompat;
import toughasnails.util.BlockStateUtils;
import toughasnails.util.config.BlockStatePredicate;
import toughasnails.util.config.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TANConfig
{
    public static HashMap<String, ArrayList<BlockTemperatureData>> blockTemperatureData = Maps.newHashMap();
    public static MaterialTemperatureData materialTemperatureData;

    public static void init(File configDir)
    {
        //Block temperature config:
        List<BlockTemperatureData> defaultBlockTemperatureData = Lists.newArrayList(
                new BlockTemperatureData(new BlockStatePredicate(TANBlocks.campfire.getDefaultState().withProperty(BlockTANCampfire.BURNING, true), Sets.newHashSet(BlockTANCampfire.BURNING)), 15.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.LIT_FURNACE), 10.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.LAVA), 20.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.FLOWING_LAVA), 20.0F),
                new BlockTemperatureData(new BlockStatePredicate(Blocks.MAGMA), 17.0F));

        if (ModCompat.HOT_SPRING_WATER != null) defaultBlockTemperatureData.add(new BlockTemperatureData(new BlockStatePredicate(ModCompat.HOT_SPRING_WATER), 15.0F));

        List<BlockTemperatureData> blockTempDataList = JsonUtil.getOrCreateConfigFile(configDir, "block_temperature.json", defaultBlockTemperatureData, new TypeToken<List<BlockTemperatureData>>(){}.getType());

        for (BlockTemperatureData tempData : blockTempDataList)
        {
            String blockName = tempData.predicate.getBlock().getRegistryName().toString();

            if (!blockTemperatureData.containsKey(blockName))
            {
                blockTemperatureData.put(blockName, new ArrayList<>());
            }

            blockTemperatureData.get(blockName).add(tempData);
        }

        //Material temperature config:
        materialTemperatureData = JsonUtil.getOrCreateConfigFile(configDir, "material_temperature.json", new MaterialTemperatureData(), new TypeToken<MaterialTemperatureData>(){}.getType());
    }
}

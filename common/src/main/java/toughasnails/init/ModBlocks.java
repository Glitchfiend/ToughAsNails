/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.block.RainCollectorBlock;
import toughasnails.block.WaterPurifierBlock;

import java.util.function.BiConsumer;

public class ModBlocks
{
    public static void registerBlocks(BiConsumer<ResourceLocation, Block> func)
    {
        TANBlocks.RAIN_COLLECTOR = register(func, "rain_collector", new RainCollectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion()));
        TANBlocks.WATER_PURIFIER = register(func, "water_purifier", new WaterPurifierBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).noOcclusion()));
    }

    private static Block register(BiConsumer<ResourceLocation, Block> func, String name, Block block)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), block);
        return block;
    }
}


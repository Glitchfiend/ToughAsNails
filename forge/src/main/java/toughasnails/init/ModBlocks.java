/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.IRegistryEventContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.block.RainCollectorBlock;
import toughasnails.block.WaterPurifierBlock;

public class ModBlocks
{
    public static void registerBlocks(IRegistryEventContext<Block> context)
    {
        TANBlocks.RAIN_COLLECTOR = register(context, "rain_collector", new RainCollectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion()));
        TANBlocks.WATER_PURIFIER = register(context, "water_purifier", new WaterPurifierBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).noOcclusion()));
    }

    private static Block register(IRegistryEventContext<Block> context, String name, Block block)
    {
        return context.register(new ResourceLocation(TANAPI.MOD_ID, name), block);
    }
}


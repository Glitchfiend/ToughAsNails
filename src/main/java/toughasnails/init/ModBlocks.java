/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.block.TANBlocks;
import toughasnails.block.RainCollectorBlock;
import toughasnails.block.WaterPurifierBlock;
import toughasnails.core.ToughAsNails;

import java.util.function.Supplier;

public class ModBlocks
{
    public static void init()
    {
        registerBlocks();
    }

    private static void registerBlocks()
    {
        TANBlocks.RAIN_COLLECTOR = register("rain_collector", () -> new RainCollectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion()));
        TANBlocks.WATER_PURIFIER = register("water_purifier", () -> new WaterPurifierBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).noOcclusion()));
    }

    public static RegistryObject<Block> register(String name, Supplier<Block> block)
    {
        RegistryObject<Block> blockRegistryObject = ToughAsNails.BLOCK_REGISTER.register(name, block);
        ToughAsNails.ITEM_REGISTER.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties()));
        return blockRegistryObject;
    }
}


/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.client.RegisterColorsEvent;
import glitchcore.util.RenderTypeHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.block.RainCollectorBlock;
import toughasnails.block.TemperatureGaugeBlock;
import toughasnails.block.WaterPurifierBlock;

import java.util.function.BiConsumer;

import static toughasnails.api.block.TANBlocks.RAIN_COLLECTOR;
import static toughasnails.api.block.TANBlocks.WATER_PURIFIER;

public class ModBlocks
{
    public static void registerBlocks(BiConsumer<ResourceLocation, Block> func)
    {
        TANBlocks.TEMPERATURE_GAUGE = register(func, "temperature_gauge", new TemperatureGaugeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.0F)));
        TANBlocks.RAIN_COLLECTOR = register(func, "rain_collector", new RainCollectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion()));
        TANBlocks.WATER_PURIFIER = register(func, "water_purifier", new WaterPurifierBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5F).noOcclusion()));
    }

    public static void registerRenderers()
    {
        RenderType transparentRenderType = RenderType.cutoutMipped();
        RenderType cutoutRenderType = RenderType.cutout();
        RenderType translucentRenderType = RenderType.translucent();

        RenderTypeHelper.setRenderType(RAIN_COLLECTOR, cutoutRenderType);
        RenderTypeHelper.setRenderType(WATER_PURIFIER, cutoutRenderType);
    }

    public static void registerBlockColors(RegisterColorsEvent.Block event)
    {
        event.register((state, world, pos, tintIndex) -> 0x47DAFF, TANBlocks.RAIN_COLLECTOR);
    }

    private static Block register(BiConsumer<ResourceLocation, Block> func, String name, Block block)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), block);
        return block;
    }
}


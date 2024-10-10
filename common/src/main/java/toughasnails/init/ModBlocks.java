/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.block.RainCollectorBlock;
import toughasnails.block.TemperatureGaugeBlock;
import toughasnails.block.ThermoregulatorBlock;
import toughasnails.block.WaterPurifierBlock;

import java.util.function.BiConsumer;

public class ModBlocks
{
    public static void registerBlocks(BiConsumer<ResourceLocation, Block> func)
    {
        TANBlocks.THERMOREGULATOR = register(func, "thermoregulator", new ThermoregulatorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F).lightLevel(ThermoregulatorBlock.lightLevel(6))));
        TANBlocks.TEMPERATURE_GAUGE = register(func, "temperature_gauge", new TemperatureGaugeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.0F)));
        TANBlocks.RAIN_COLLECTOR = register(func, "rain_collector", new RainCollectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
        TANBlocks.WATER_PURIFIER = register(func, "water_purifier", new WaterPurifierBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).requiresCorrectToolForDrops().strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
    }

    private static Block register(BiConsumer<ResourceLocation, Block> func, String name, Block block)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), block);
        return block;
    }
}


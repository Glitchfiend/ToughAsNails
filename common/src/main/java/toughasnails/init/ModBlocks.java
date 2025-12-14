/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CaveVines;
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
import toughasnails.core.ToughAsNails;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModBlocks
{
    public static void registerBlocks(BiConsumer<Identifier, Block> func)
    {
        TANBlocks.THERMOREGULATOR = register(func, "thermoregulator", ThermoregulatorBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F).lightLevel(ThermoregulatorBlock.lightLevel(6)));
        TANBlocks.TEMPERATURE_GAUGE = register(func, "temperature_gauge", TemperatureGaugeBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.0F));
        TANBlocks.RAIN_COLLECTOR = register(func, "rain_collector", RainCollectorBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).noOcclusion());
        TANBlocks.WATER_PURIFIER = register(func, "water_purifier", WaterPurifierBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).requiresCorrectToolForDrops().strength(2.5F).sound(SoundType.WOOD).noOcclusion());
    }

    private static Block register(BiConsumer<Identifier, Block> func, ResourceKey<Block> key, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties)
    {
        Block block = factory.apply(properties.setId(key));
        func.accept(key.identifier(), block);
        return block;
    }

    private static Block register(BiConsumer<Identifier, Block> func, String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties)
    {
        return register(func, blockId(name), factory, properties);
    }

    private static ResourceKey<Block> blockId(String name)
    {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, name));
    }
}


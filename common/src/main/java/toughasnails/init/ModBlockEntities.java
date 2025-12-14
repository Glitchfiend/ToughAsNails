/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.block.entity.TemperatureGaugeBlockEntity;
import toughasnails.block.entity.ThermoregulatorBlockEntity;
import toughasnails.block.entity.WaterPurifierBlockEntity;
import toughasnails.core.ToughAsNails;

import java.util.Set;
import java.util.function.BiConsumer;

public class ModBlockEntities
{
    public static void registerBlockEntities(BiConsumer<Identifier, BlockEntityType<?>> func)
    {
        TANBlockEntityTypes.WATER_PURIFIER = register(func, "water_purifier", WaterPurifierBlockEntity::new, Set.of(TANBlocks.WATER_PURIFIER));
        TANBlockEntityTypes.TEMPERATURE_GAUGE = register(func, "temperature_gauge", TemperatureGaugeBlockEntity::new, Set.of(TANBlocks.TEMPERATURE_GAUGE));
        TANBlockEntityTypes.THERMOREGULATOR = register(func, "thermoregulator", ThermoregulatorBlockEntity::new, Set.of(TANBlocks.THERMOREGULATOR));
    }


    private static <T extends BlockEntity> BlockEntityType<?> register(BiConsumer<Identifier, BlockEntityType<?>> func, String name, BlockEntityType.BlockEntitySupplier<T> supplier, Set<Block> blocks)
    {
        var type = new BlockEntityType(supplier, blocks);
        func.accept(Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, name), type);
        return type;
    }
}

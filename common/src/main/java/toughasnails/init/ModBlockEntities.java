/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.block.entity.TemperatureGaugeBlockEntity;
import toughasnails.block.entity.ThermoregulatorBlockEntity;
import toughasnails.block.entity.WaterPurifierBlockEntity;

import java.util.function.BiConsumer;

public class ModBlockEntities
{
    public static void registerBlockEntities(BiConsumer<ResourceLocation, BlockEntityType<?>> func)
    {
        TANBlockEntityTypes.WATER_PURIFIER = register(func, "water_purifier", BlockEntityType.Builder.of(WaterPurifierBlockEntity::new, TANBlocks.WATER_PURIFIER));
        TANBlockEntityTypes.TEMPERATURE_GAUGE = register(func, "temperature_gauge", BlockEntityType.Builder.of(TemperatureGaugeBlockEntity::new, TANBlocks.TEMPERATURE_GAUGE));
        TANBlockEntityTypes.THERMOREGULATOR = register(func, "thermoregulator", BlockEntityType.Builder.of(ThermoregulatorBlockEntity::new, TANBlocks.THERMOREGULATOR));
    }

    private static <T extends BlockEntity> BlockEntityType<?> register(BiConsumer<ResourceLocation, BlockEntityType<?>> func, String name, BlockEntityType.Builder<T> builder)
    {
        var type = builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, name));
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), type);
        return type;
    }
}

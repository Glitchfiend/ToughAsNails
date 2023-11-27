/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.block.entity.WaterPurifierBlockEntity;
import toughasnails.core.ToughAsNails;

import java.util.function.Supplier;

public class ModBlockEntities
{
    public static void init()
    {
        registerTileEntities();
    }

    private static void registerTileEntities()
    {
        TANBlockEntityTypes.WATER_PURIFIER = register("water_purifier", () -> BlockEntityType.Builder.of(WaterPurifierBlockEntity::new, TANBlocks.WATER_PURIFIER.get()));
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<?>> register(String name, Supplier<BlockEntityType.Builder<T>> builder)
    {
        Supplier<BlockEntityType<?>> supplier = () -> {
            Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, name);
            return builder.get().build(type);
        };

        return ToughAsNails.BLOCK_ENTITY_REGISTER.register(name, supplier);
    }
}

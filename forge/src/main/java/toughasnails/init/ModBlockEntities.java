/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.mojang.datafixers.types.Type;
import glitchcore.event.IRegistryEventContext;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.block.entity.WaterPurifierBlockEntity;

public class ModBlockEntities
{
    public static void registerTileEntities(IRegistryEventContext<BlockEntityType<?>> context)
    {
        TANBlockEntityTypes.WATER_PURIFIER = register(context, "water_purifier", BlockEntityType.Builder.of(WaterPurifierBlockEntity::new, TANBlocks.WATER_PURIFIER));
    }

    private static <T extends BlockEntity> BlockEntityType<?> register(IRegistryEventContext<BlockEntityType<?>> context, String name, BlockEntityType.Builder<T> builder)
    {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, name);
        return context.register(new ResourceLocation(TANAPI.MOD_ID, name), builder.build(type));
    }
}

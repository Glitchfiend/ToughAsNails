/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.mojang.datafixers.types.Type;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.api.block.TANBlocks;
import toughasnails.tileentity.WaterPurifierTileEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities
{
    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event)
    {
        register("water_purifier", BlockEntityType.Builder.of(WaterPurifierTileEntity::new, TANBlocks.WATER_PURIFIER));
    }

    public static <T extends BlockEntity> void register(String name, BlockEntityType.Builder<T> builder)
    {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, name);
        BlockEntityType<?> tileEntityType = builder.build(type);
        tileEntityType.setRegistryName(name);
        ForgeRegistries.TILE_ENTITIES.register(tileEntityType);
    }
}

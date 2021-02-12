/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
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
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event)
    {
        register("water_purifier", TileEntityType.Builder.of(WaterPurifierTileEntity::new, TANBlocks.WATER_PURIFIER));
    }

    public static <T extends TileEntity> void register(String name, TileEntityType.Builder<T> builder)
    {
        Type<?> type = Util.fetchChoiceType(TypeReferences.BLOCK_ENTITY, name);
        TileEntityType<?> tileEntityType = builder.build(type);
        tileEntityType.setRegistryName(name);
        ForgeRegistries.TILE_ENTITIES.register(tileEntityType);
    }
}

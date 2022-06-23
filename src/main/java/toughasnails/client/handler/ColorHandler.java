/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.item.DyeableWoolItem;
import toughasnails.item.LeafArmorItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorHandler
{
    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        ItemColors itemColors = event.getItemColors();

        itemColors.register((stack, tintIndex) -> {
            return tintIndex > 0 ? -1 : ((DyeableWoolItem)stack.getItem()).getColor(stack);
        }, TANItems.WOOL_HELMET.get(), TANItems.WOOL_CHESTPLATE.get(), TANItems.WOOL_LEGGINGS.get(), TANItems.WOOL_BOOTS.get());

        itemColors.register((stack, tintIndex) -> {
            return tintIndex > 0 ? -1 : ((LeafArmorItem)stack.getItem()).getColor(stack);
        }, TANItems.LEAF_HELMET.get(), TANItems.LEAF_CHESTPLATE.get(), TANItems.LEAF_LEGGINGS.get(), TANItems.LEAF_BOOTS.get());
    }

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event)
    {
        BlockColors blockColors = event.getBlockColors();
        blockColors.register((state, world, pos, tintIndex) -> 0x47DAFF, TANBlocks.RAIN_COLLECTOR.get());
    }
}

/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.item.DyeableWoolItem;
import toughasnails.item.LeafArmorItem;

public class ColorHandler
{
    @SubscribeEvent
    public void registerItemColors(ColorHandlerEvent.Item event)
    {
        ItemColors itemColors = event.getItemColors();

        itemColors.register((stack, tintIndex) -> {
            return tintIndex > 0 ? -1 : ((DyeableWoolItem)stack.getItem()).getColor(stack);
        }, TANItems.WOOL_HELMET, TANItems.WOOL_CHESTPLATE, TANItems.WOOL_LEGGINGS, TANItems.WOOL_BOOTS);

        itemColors.register((stack, tintIndex) -> {
            return tintIndex > 0 ? -1 : ((LeafArmorItem)stack.getItem()).getColor(stack);
        }, TANItems.LEAF_HELMET, TANItems.LEAF_CHESTPLATE, TANItems.LEAF_LEGGINGS, TANItems.LEAF_BOOTS);
    }

    @SubscribeEvent
    public void registerBlockColors(ColorHandlerEvent.Block event)
    {
        BlockColors blockColors = event.getBlockColors();
        blockColors.register((state, world, pos, tintIndex) -> 0x47DAFF, TANBlocks.RAIN_COLLECTOR);
    }
}

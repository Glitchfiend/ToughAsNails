/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;

import java.lang.reflect.Field;

public class ModCreativeTab
{
    public static void setup()
    {
        ToughAsNails.CREATIVE_TAB_REGISTER.register("main", () ->
        {
            var ITEM_BLACKLIST = ImmutableList.of(TANItems.TAN_ICON);
            var BLOCK_BLACKLIST = ImmutableList.of();

            return CreativeModeTab.builder()
                .icon(() -> new ItemStack(TANItems.TAN_ICON))
                .title(Component.translatable("itemGroup.toughasnails"))
                .displayItems((displayParameters, output) ->
                {
                    // Add blocks
                    for (Field field : TANBlocks.class.getFields())
                    {
                        if (field.getType() != Block.class) continue;

                        try
                        {
                            Block block = (Block) field.get(null);
                            if (!BLOCK_BLACKLIST.contains(block))
                                output.accept(new ItemStack(block));
                        }
                        catch (IllegalAccessException e)
                        {
                        }
                    }

                    // Add items
                    for (Field field : TANItems.class.getFields())
                    {
                        if (field.getType() != Item.class) continue;

                        try
                        {
                            Item item = (Item) field.get(null);
                            if (!ITEM_BLACKLIST.contains(item))
                                output.accept(new ItemStack(item));
                        }
                        catch (IllegalAccessException e)
                        {
                        }
                    }
                }).build();
        });
    }
}
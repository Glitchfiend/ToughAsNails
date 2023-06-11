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
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;

import java.lang.reflect.Field;
import java.util.List;

public class ModCreativeTab
{
    private static final List<RegistryObject<Item>> ITEM_BLACKLIST = ImmutableList.of(TANItems.TAN_ICON);

    private static final List<RegistryObject<Block>> BLOCK_BLACKLIST = ImmutableList.of();

    public static void setup()
    {
        ToughAsNails.CREATIVE_TAB_REGISTER.register("main", () ->
        CreativeModeTab.builder()
            .icon(() -> new ItemStack(TANItems.TAN_ICON.get()))
            .title(Component.translatable("itemGroup.toughasnails"))
            .displayItems((displayParameters, output) -> {
                // Add blocks
                for (Field field : TANBlocks.class.getFields())
                {
                    if (field.getType() != RegistryObject.class) continue;

                    try
                    {
                        RegistryObject<Block> block = (RegistryObject)field.get(null);
                        if (!BLOCK_BLACKLIST.contains(block))
                            output.accept(new ItemStack(block.get()));
                    }
                    catch (IllegalAccessException e) {}
                }

                // Add items
                for (Field field : TANItems.class.getFields())
                {
                    if (field.getType() != RegistryObject.class) continue;

                    try
                    {
                        RegistryObject<Item> item = (RegistryObject)field.get(null);
                        if (!ITEM_BLACKLIST.contains(item))
                            output.accept(new ItemStack(item.get()));
                    }
                    catch (IllegalAccessException e) {}
                }
            }).build()
        );
    }
}
/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import toughasnails.api.TANAPI;
import toughasnails.api.item.TANItems;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class ModCreativeTab
{
    public static void registerCreativeTabs(BiConsumer<ResourceLocation, CreativeModeTab> func)
    {
        var ITEM_BLACKLIST = ImmutableList.of(TANItems.TAN_ICON);
        var tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(TANItems.TAN_ICON))
            .title(Component.translatable("itemGroup.toughasnails"))
            .displayItems((displayParameters, output) ->
            {
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

        register(func, "main", tab);
    }

    private static CreativeModeTab register(BiConsumer<ResourceLocation, CreativeModeTab> func, String name, CreativeModeTab tab)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), tab);
        return tab;
    }
}
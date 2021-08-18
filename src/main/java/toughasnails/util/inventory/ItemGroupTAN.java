/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.util.inventory;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import toughasnails.api.item.TANItems;

public class ItemGroupTAN extends CreativeModeTab
{
    public static final ItemGroupTAN INSTANCE = new ItemGroupTAN(CreativeModeTab.TABS.length, "toughasnails");

    private ItemGroupTAN(int index, String label)
    {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon()
    {
        return new ItemStack(TANItems.TAN_ICON);
    }
}

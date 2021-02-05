/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.util.inventory;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import toughasnails.api.item.TANItems;

public class ItemGroupTAN extends ItemGroup
{
    public static final ItemGroupTAN INSTANCE = new ItemGroupTAN(ItemGroup.TABS.length, "toughasnails");

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

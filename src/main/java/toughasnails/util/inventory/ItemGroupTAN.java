/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
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

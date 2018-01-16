/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.inventory;

import glitchcore.util.GFNonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class GFCreativeTab extends CreativeTabs
{
    public GFCreativeTab(String label)
    {
        super(label);
    }
    public GFCreativeTab(int index, String label) { super(index, label); }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> list)
    {
        GFNonNullList<ItemStack> itemList = GFNonNullList.create();
        this.displayAllRelevantItems(itemList);
        list.addAll(itemList);
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return this.getTabIconItem();
    }

    // Introduced in 1.12
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(GFNonNullList<ItemStack> itemList)
    {
        for (Item item : Item.REGISTRY)
        {
            item.getSubItems(this, itemList);
        }
    }
}

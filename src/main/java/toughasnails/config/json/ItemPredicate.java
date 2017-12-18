/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config.json;

import com.google.common.base.Predicate;
import com.google.gson.annotations.SerializedName;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ItemPredicate implements Predicate<ItemStack>
{
    @SerializedName("name")
    private String itemRegistryName;
    private int metadata;

    public ItemPredicate(ResourceLocation itemRegistryLoc, int metadata)
    {
        this.itemRegistryName = itemRegistryLoc.toString();
        this.metadata = metadata;
    }

    public ItemPredicate(Item item, int metadata)
    {
        this(item.getRegistryName(), metadata);
    }

    public ItemPredicate(Item item)
    {
        this(item, 0);
    }

    @Override
    public boolean apply(@Nullable ItemStack input)
    {
        return input.isItemEqual(this.getItemStack());
    }

    public ItemStack getItemStack()
    {
        Item item = Item.getByNameOrId(this.itemRegistryName);

        if (item != null)
        {
            return new ItemStack(item, 1, this.metadata);
        }

        return null;
    }
}

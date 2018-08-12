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
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import toughasnails.core.ToughAsNails;
import toughasnails.util.config.NBTUtilExt;

import javax.annotation.Nullable;

public class ItemPredicate implements Predicate<ItemStack>
{
    @SerializedName("name")
    private String itemRegistryName;
    private int metadata;
    private String nbt;

    public ItemPredicate(ResourceLocation itemRegistryLoc, int metadata, String nbt) {
        this.itemRegistryName = itemRegistryLoc.toString();
        this.metadata = metadata;
    }

    public ItemPredicate(ResourceLocation itemRegistryLoc, int metadata)
    {
        this(itemRegistryLoc, metadata, null);
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
        ItemStack stack = this.getItemStack();
        boolean areItemsEqual = input.isItemEqual(stack);
        NBTTagCompound ourTag = stack.getTagCompound();
        NBTTagCompound theirTag = input.getTagCompound();
        boolean areTagsEqual = NBTUtilExt.areNBTsEqualOrNull(ourTag, theirTag);

        return areItemsEqual && areTagsEqual;
    }

    public ItemStack getItemStack()
    {
        Item item = Item.getByNameOrId(this.itemRegistryName);

        if (item != null)
        {
            ItemStack stack = new ItemStack(item, 1, this.metadata);
            if (nbt != null) {
                try {
                    stack.setTagCompound(JsonToNBT.getTagFromJson(nbt));
                } catch (NBTException e) {
                    ToughAsNails.logger.error("Failed to parse NBT tag for ItemPredicate: ignoring NBT.  " +
                            "This is likely an error. (Item Name: " + itemRegistryName + ")");
                }
            }
            return stack;
        }

        return null;
    }
}

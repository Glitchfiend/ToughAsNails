/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.item;

import glitchcore.util.GFNonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemHelper
{
    public static void registerItem(ResourceLocation location, Item item)
    {
        item.setRegistryName(location);
        ForgeRegistries.ITEMS.register(item);
    }

    public static void setRepairMaterial(ItemArmor.ArmorMaterial material, ItemStack stack)
    {
        material.repairMaterial = stack;
    }

    public static GFNonNullList<ItemStack> getSubItems(CreativeTabs tab, Item item)
    {
        GFNonNullList<ItemStack> list = GFNonNullList.create();
        item.getSubItems(tab, list);
        return list;
    }

    public static ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        return stack.getItem().onItemRightClick(world, player, hand);
    }
}

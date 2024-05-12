/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.enchantment;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import toughasnails.item.EmptyCanteenItem;

public class WaterCleansingEnchantment extends Enchantment
{
    public WaterCleansingEnchantment()
    {
        super(Enchantment.definition(ItemTags.DURABILITY_ENCHANTABLE, 2, 1, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4, EquipmentSlot.MAINHAND));
    }

    @Override
    public boolean isTreasureOnly()
    {
        return true;
    }

    @Override
    public boolean canEnchant(ItemStack stack)
    {
        return stack.getItem() instanceof EmptyCanteenItem;
    }
}

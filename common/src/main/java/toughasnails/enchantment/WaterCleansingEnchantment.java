/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import toughasnails.item.EmptyCanteenItem;

public class WaterCleansingEnchantment extends Enchantment
{
    public WaterCleansingEnchantment()
    {
        // EnchantmentCategory is irrelevant for treasure only enchantments
        super(Enchantment.Rarity.RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[] { EquipmentSlot.MAINHAND } );
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

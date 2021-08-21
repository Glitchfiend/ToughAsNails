/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import toughasnails.api.enchantment.TANEnchantments;

public class CoolingEnchantment extends Enchantment
{
    public CoolingEnchantment()
    {
        super(Rarity.COMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET } );
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment)
    {
        return this != enchantment && enchantment != TANEnchantments.WARMING;
    }
}

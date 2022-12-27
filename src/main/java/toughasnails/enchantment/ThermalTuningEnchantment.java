/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ThermalTuningEnchantment extends Enchantment
{
    public ThermalTuningEnchantment()
    {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[] { EquipmentSlot.CHEST } );
    }

    @Override
    public boolean isTreasureOnly()
    {
        return true;
    }
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import toughasnails.api.enchantment.TANEnchantments;

public class TemperatureTuningEnchantment extends Enchantment
{
    public TemperatureTuningEnchantment()
    {
        super(Rarity.RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[] { EquipmentSlot.CHEST } );
    }

    @Override
    public boolean isTreasureOnly()
    {
        return true;
    }
}

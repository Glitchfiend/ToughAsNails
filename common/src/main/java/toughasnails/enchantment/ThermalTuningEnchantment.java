/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.enchantment;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class ThermalTuningEnchantment extends Enchantment
{
    public ThermalTuningEnchantment()
    {
        super(Enchantment.definition(ItemTags.CHEST_ARMOR_ENCHANTABLE, 2, 1, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4, EquipmentSlot.CHEST));
    }

    @Override
    public boolean isTreasureOnly()
    {
        return true;
    }
}

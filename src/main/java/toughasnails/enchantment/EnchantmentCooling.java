package toughasnails.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import toughasnails.init.ModEnchantments;

public class EnchantmentCooling extends Enchantment {
    public EnchantmentCooling() {
        super(Rarity.COMMON, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET});
        this.setRegistryName("cooling");
        this.setName("cooling");
    }

    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != ModEnchantments.WARMING;
    }
}

package toughasnails.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import toughasnails.init.ModEnchantments;

public class EnchantmentWarming extends Enchantment {
    public EnchantmentWarming() {
        super(Rarity.COMMON, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET});
        this.setRegistryName("warming");
        this.setName("warming");
    }

    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != ModEnchantments.COOLING;
    }
}

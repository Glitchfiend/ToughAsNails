package toughasnails.init;

import net.minecraft.util.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

public class ModArmorMaterials
{
    public static final ArmorMaterial WOOL = new ArmorMaterial(5, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 1);
        map.put(ArmorType.CHESTPLATE, 1);
        map.put(ArmorType.HELMET, 1);
    }), 1, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, ItemTags.WOOL, ModEquipmentAssets.WOOL);

    public static ArmorMaterial LEAF = new ArmorMaterial(5, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 1);
        map.put(ArmorType.CHESTPLATE, 1);
        map.put(ArmorType.HELMET, 1);
    }), 1, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, ItemTags.LEAVES, ModEquipmentAssets.LEAF);
}

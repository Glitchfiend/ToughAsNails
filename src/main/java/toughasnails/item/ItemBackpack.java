package toughasnails.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemBackpack extends ItemArmor
{
    public ItemBackpack(ItemArmor.ArmorMaterial material, int renderIndex)
    {
        // backpacks are always on your body - armorType = 1
        super(material, renderIndex, EntityEquipmentSlot.CHEST);        
    }
}

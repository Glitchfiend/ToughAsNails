package toughasnails.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemRespirator extends ItemArmor
{
    public ItemRespirator(ItemArmor.ArmorMaterial material, int renderIndex)
    {
        // respirators are always on your head - armorType = 0
        super(material, renderIndex, EntityEquipmentSlot.HEAD);   
        
        this.setCreativeTab(null);
    }
}

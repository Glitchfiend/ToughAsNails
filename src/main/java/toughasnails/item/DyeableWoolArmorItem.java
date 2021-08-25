package toughasnails.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public class DyeableWoolArmorItem extends ArmorItem implements DyeableWoolItem
{
    public DyeableWoolArmorItem(ArmorMaterial p_41091_, EquipmentSlot p_41092_, Item.Properties p_41093_)
    {
        super(p_41091_, p_41092_, p_41093_);
    }
}
package toughasnails.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public interface DyeableWoolItem extends DyeableLeatherItem
{
    @Override
    default int getColor(ItemStack p_41122_)
    {
        CompoundTag compoundtag = p_41122_.getTagElement("display");
        return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : 0xFFFFFF;
    }
}

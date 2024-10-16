package toughasnails.item;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;

public class LeafArmorItem extends DyeableArmorItem
{
    public LeafArmorItem(ArmorMaterial p_41091_, ArmorItem.Type type, Item.Properties p_41093_)
    {
        super(p_41091_, type, p_41093_);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected)
    {
        if (!world.isClientSide())
            return;

        int color = BiomeColors.getAverageFoliageColor(world, entity.blockPosition());
        this.setColor(stack, color);
    }

    @Override
    public int getColor(ItemStack p_41122_)
    {
        CompoundTag compoundtag = p_41122_.getTagElement("display");
        return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : FoliageColor.getDefaultColor();
    }

    @Override
    public void clearColor(ItemStack p_41124_)
    {
    }
}

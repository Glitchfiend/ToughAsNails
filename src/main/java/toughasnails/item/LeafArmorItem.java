package toughasnails.item;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;

import java.util.List;

public class LeafArmorItem extends ArmorItem implements DyeableLeatherItem
{
    public LeafArmorItem(ArmorMaterial p_41091_, EquipmentSlot p_41092_, Item.Properties p_41093_)
    {
        super(p_41091_, p_41092_, p_41093_);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player)
    {
        int color = FoliageColor.getDefaultColor();

        if (world != null && player != null)
        {
            color = BiomeColors.getAverageFoliageColor(world, player.blockPosition());
        }

        this.setColor(stack, color);
    }

    @Override
    public int getColor(ItemStack p_41122_)
    {
        CompoundTag compoundtag = p_41122_.getTagElement("display");
        return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : FoliageColor.getDefaultColor();
    }

    @Override
    public void setColor(ItemStack p_41116_, int p_41117_)
    {
        p_41116_.getOrCreateTagElement("display").putInt("color", p_41117_);
    }

    @Override
    public void clearColor(ItemStack p_41124_)
    {
    }

    public static ItemStack dyeArmor(ItemStack p_41119_, List<DyeItem> p_41120_)
    {
        return p_41119_;
    }
}

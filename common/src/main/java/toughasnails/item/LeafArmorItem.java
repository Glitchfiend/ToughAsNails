/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import toughasnails.api.item.TANItems;

public class LeafArmorItem extends ArmorItem
{
    public LeafArmorItem(Holder<ArmorMaterial> $$0, Type $$1, Properties $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_)
    {
        if (!level.isClientSide())
            return;

        if (stack.getItem() == TANItems.LEAF_BOOTS || stack.getItem() == TANItems.LEAF_LEGGINGS || stack.getItem() == TANItems.LEAF_CHESTPLATE || stack.getItem() == TANItems.LEAF_HELMET)
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(BiomeColors.getAverageFoliageColor(entity.level(), entity.blockPosition()), false));
    }
}

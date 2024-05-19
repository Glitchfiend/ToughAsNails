/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class WoolArmorItem extends ArmorItem
{
    public WoolArmorItem(Holder<ArmorMaterial> $$0, Type $$1, Properties $$2)
    {
        super($$0, $$1, $$2);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack)
    {
        if (!stack.has(DataComponents.DYED_COLOR))
        {
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF, false));
        }
    }
}

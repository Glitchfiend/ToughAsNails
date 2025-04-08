/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class WoolArmorItem extends Item
{
    public WoolArmorItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack)
    {
        if (!stack.has(DataComponents.DYED_COLOR))
        {
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF));
        }
    }
}

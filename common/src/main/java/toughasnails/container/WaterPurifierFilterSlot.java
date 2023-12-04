/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WaterPurifierFilterSlot extends Slot
{
    private final WaterPurifierContainer menu;

    public WaterPurifierFilterSlot(WaterPurifierContainer container, Container inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
        this.menu = container;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return this.menu.isFilter(stack);
    }
}

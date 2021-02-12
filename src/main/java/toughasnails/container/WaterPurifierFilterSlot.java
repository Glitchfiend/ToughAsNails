/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WaterPurifierFilterSlot extends Slot
{
    private final WaterPurifierContainer menu;

    public WaterPurifierFilterSlot(WaterPurifierContainer container, IInventory inventory, int index, int xPosition, int yPosition)
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

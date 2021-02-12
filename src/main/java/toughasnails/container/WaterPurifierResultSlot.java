/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WaterPurifierResultSlot extends Slot
{
    public WaterPurifierResultSlot(IInventory inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
    }

    // Prevent placing item in slot
    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return false;
    }
}

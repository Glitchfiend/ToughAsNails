/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WaterPurifierResultSlot extends Slot
{
    public WaterPurifierResultSlot(Container inventory, int index, int xPosition, int yPosition)
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

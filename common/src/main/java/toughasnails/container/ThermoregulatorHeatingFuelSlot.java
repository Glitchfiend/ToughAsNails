/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import toughasnails.block.entity.ThermoregulatorBlockEntity;

public class ThermoregulatorHeatingFuelSlot extends Slot
{
    public ThermoregulatorHeatingFuelSlot(Container inventory, int index, int xPosition, int yPosition)
    {
        super(inventory, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return ThermoregulatorBlockEntity.isHeatingFuel(stack);
    }
}

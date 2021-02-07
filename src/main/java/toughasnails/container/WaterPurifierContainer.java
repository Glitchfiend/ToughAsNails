/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceFuelSlot;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import toughasnails.api.inventory.container.TANContainerTypes;

public class WaterPurifierContainer extends Container
{
    private final IInventory container;

    public WaterPurifierContainer(int id, PlayerInventory playerInventory)
    {
        this(id, playerInventory, new Inventory(3));
    }

    public WaterPurifierContainer(int id, PlayerInventory playerInventory, IInventory container)
    {
        super(TANContainerTypes.WATER_PURIFIER, id);
        this.container = container;

        // Add input item slot
        this.addSlot(new Slot(container, 0, 56, 17));

        // Add filter item slot
        this.addSlot(new Slot(container, 1, 56, 53));

        // Add output item slot
        this.addSlot(new Slot(container, 2, 116, 35));

        // Add inventory slots
        for (int row = 0; row < 3; ++row)
        {
            for (int col = 0; col < 9; ++col)
            {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Add hotbar slots
        for (int slot = 0; slot < 9; ++slot)
        {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 142));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return this.container.stillValid(player);
    }
}

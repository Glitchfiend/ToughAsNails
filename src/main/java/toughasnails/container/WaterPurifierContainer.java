/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import toughasnails.api.inventory.container.TANContainerTypes;

public class WaterPurifierContainer extends Container
{
    private final IInventory container;

    public WaterPurifierContainer(int id, PlayerInventory inventory)
    {
        super(TANContainerTypes.WATER_PURIFIER, id);
        this.container = inventory;
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return this.container.stillValid(player);
    }
}

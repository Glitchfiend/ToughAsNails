/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import toughasnails.api.container.TANContainerTypes;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.block.entity.ThermoregulatorBlockEntity;
import toughasnails.block.entity.WaterPurifierBlockEntity;
import toughasnails.crafting.WaterPurifierRecipe;

public class ThermoregulatorContainer extends AbstractContainerMenu
{
    private final Container container;
    private final ContainerData data;
    private final Level level;

    public ThermoregulatorContainer(int id, Inventory playerInventory)
    {
        this(id, playerInventory, new SimpleContainer(2), new SimpleContainerData(2));
    }

    public ThermoregulatorContainer(int id, Inventory playerInventory, Container container, ContainerData data)
    {
        super(TANContainerTypes.THERMOREGULATOR, id);
        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level();

        // Add input item slot
        this.addSlot(new ThermoregulatorCoolingFuelSlot(container, ThermoregulatorBlockEntity.SLOT_COOLING, 44, 42));

        // Add filter item slot
        this.addSlot(new ThermoregulatorHeatingFuelSlot(container, ThermoregulatorBlockEntity.SLOT_HEATING, 116, 42));

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

        // Add data slots
        this.addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return this.container.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index)
    {
        ItemStack prevItem = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem())
        {
            ItemStack slotItem = slot.getItem();
            prevItem = slotItem.copy();

            // Output
            if (index != ThermoregulatorBlockEntity.SLOT_HEATING && index != ThermoregulatorBlockEntity.SLOT_COOLING) // Moving from inventory to the purifier
            {
                if (ThermoregulatorBlockEntity.isCoolingFuel(slotItem))
                {
                    if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (ThermoregulatorBlockEntity.isHeatingFuel(slotItem))
                {
                    if (!this.moveItemStackTo(slotItem, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 2 && index < 30)
                {
                    if (!this.moveItemStackTo(slotItem, 30, 38, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 38 && !this.moveItemStackTo(slotItem, 2, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotItem, 2, 38, false)) // Move from purifier to inventory
            {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            // No change
            if (slotItem.getCount() == prevItem.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotItem);
        }

        return prevItem;
    }

    public int getCoolingFuelProgress()
    {
        return this.data.get(0) * 13 / ThermoregulatorBlockEntity.CONSUMABLE_DURATION;
    }

    public int getHeatingFuelProgress()
    {
        return this.data.get(1) * 13 / ThermoregulatorBlockEntity.CONSUMABLE_DURATION;
    }

    public boolean isCooling()
    {
        return this.data.get(0) > 0;
    }

    public boolean isHeating()
    {
        return this.data.get(1) > 0;
    }
}

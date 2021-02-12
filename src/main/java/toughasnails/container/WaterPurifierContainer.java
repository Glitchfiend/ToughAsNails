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
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.api.inventory.container.TANContainerTypes;
import toughasnails.crafting.WaterPurifierRecipe;
import toughasnails.tileentity.WaterPurifierTileEntity;

public class WaterPurifierContainer extends Container
{
    private final IInventory container;
    private final IIntArray data;
    private final World level;

    public WaterPurifierContainer(int id, PlayerInventory playerInventory)
    {
        this(id, playerInventory, new Inventory(3), new IntArray(4));
    }

    public WaterPurifierContainer(int id, PlayerInventory playerInventory, IInventory container, IIntArray data)
    {
        super(TANContainerTypes.WATER_PURIFIER, id);
        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level;

        // Add input item slot
        this.addSlot(new Slot(container, 0, 56, 17));

        // Add filter item slot
        this.addSlot(new WaterPurifierFilterSlot(this, container, 1, 56, 53));

        // Add output item slot
        this.addSlot(new WaterPurifierResultSlot(container, 2, 116, 35));

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
    public boolean stillValid(PlayerEntity player)
    {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index)
    {
        ItemStack prevItem = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem())
        {
            ItemStack slotItem = slot.getItem();
            prevItem = slotItem.copy();

            // Output
            if (index == 2)
            {
                if (!this.moveItemStackTo(slotItem, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotItem, prevItem);
            }
            else if (index != 1 && index != 0) // Moving from inventory to the purifier
            {
                if (this.canPurify(slotItem))
                {
                    if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (this.isFilter(slotItem))
                {
                    if (!this.moveItemStackTo(slotItem, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.moveItemStackTo(slotItem, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.moveItemStackTo(slotItem, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotItem, 3, 39, false)) // Move from purifier to inventory
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

    protected boolean canPurify(ItemStack stack)
    {
        return this.level.getRecipeManager().getRecipeFor((IRecipeType<WaterPurifierRecipe>)TANRecipeTypes.WATER_PURIFYING, new Inventory(stack), this.level).isPresent();
    }

    protected boolean isFilter(ItemStack stack)
    {
        return WaterPurifierTileEntity.isFilter(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public int getPurifyProgress()
    {
        int progress = this.data.get(2);
        int totalTime = this.data.get(3);
        return totalTime != 0 && progress != 0 ? progress * 24 / totalTime : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFilterProgress()
    {
        int filterDuration = this.data.get(1);

        // Prevent division by 0
        if (filterDuration == 0)
        {
            filterDuration = 200;
        }

        return this.data.get(0) * 13 / filterDuration;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isFiltering()
    {
        return this.data.get(0) > 0;
    }
}

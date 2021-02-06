/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import toughasnails.api.tileentity.TANTileEntityTypes;
import toughasnails.container.WaterPurifierContainer;

import javax.annotation.Nullable;

public class WaterPurifierTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity
{
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    protected final IIntArray dataAccess = new IIntArray()
    {
        @Override
        public int get(int index)
        {
            return 0;
        }

        @Override
        public void set(int index, int value)
        {

        }

        @Override
        public int getCount()
        {
            return 0;
        }
    };

    public WaterPurifierTileEntity()
    {
        super(TANTileEntityTypes.WATER_PURIFIER);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt)
    {
        super.save(nbt);
        return nbt;
    }

    @Override
    public void tick()
    {

    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new WaterPurifierContainer(id, player);
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.sereneseasons.purifier");
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return false;
    }

    @Override
    public int getContainerSize()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.items)
        {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int index)
    {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        return ItemStackHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        return ItemStackHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        this.items.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        if (this.level.getBlockEntity(this.worldPosition) != this)
        {
            return false;
        }
        else
        {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent()
    {
        this.items.clear();
    }
}

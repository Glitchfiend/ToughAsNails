/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.tileentity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.api.tileentity.TANTileEntityTypes;
import toughasnails.block.WaterPurifierBlock;
import toughasnails.container.WaterPurifierContainer;
import toughasnails.core.ToughAsNails;
import toughasnails.crafting.WaterPurifierRecipe;

import javax.annotation.Nullable;

public class WaterPurifierTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity
{
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    /** The time remaining from the previously used filter */
    private int filterTimeRemaining;

    /** The total duration associated with the previously used filter */
    private int filterDuration;

    /** The progress towards meeting the total purify time. */
    private int purifyProgress;

    /** The total time needed to complete purification. */
    private int purifyTotalTime;

    protected final IIntArray dataAccess = new IIntArray()
    {
        @Override
        public int get(int index)
        {
            switch(index) {
                case 0:
                    return WaterPurifierTileEntity.this.filterTimeRemaining;
                case 1:
                    return WaterPurifierTileEntity.this.filterDuration;
                case 2:
                    return WaterPurifierTileEntity.this.purifyProgress;
                case 3:
                    return WaterPurifierTileEntity.this.purifyTotalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value)
        {
            switch(index) {
                case 0:
                    WaterPurifierTileEntity.this.filterTimeRemaining = value;
                    break;
                case 1:
                    WaterPurifierTileEntity.this.filterDuration = value;
                    break;
                case 2:
                    WaterPurifierTileEntity.this.purifyProgress = value;
                    break;
                case 3:
                    WaterPurifierTileEntity.this.purifyTotalTime = value;
            }

        }

        @Override
        public int getCount()
        {
            return 4;
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
        this.filterTimeRemaining = nbt.getInt("FilterTimeRemaining");
        this.filterDuration = nbt.getInt("FilterDuration");
        this.purifyProgress = nbt.getInt("PurifyProgress");
        this.purifyTotalTime = nbt.getInt("PurifyTotalTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt)
    {
        super.save(nbt);
        nbt.putInt("FilterTimeRemaining", this.filterTimeRemaining);
        nbt.putInt("FilterDuration", this.filterDuration);
        nbt.putInt("PurifyProgress", this.purifyProgress);
        nbt.putInt("PurifyTotalTime", this.purifyTotalTime);
        ItemStackHelper.saveAllItems(nbt, this.items);
        return nbt;
    }

    @Override
    public void tick()
    {
        boolean previouslyFiltering = this.currentlyFiltering();
        boolean changed = false;

        // Reduce the time remaining on the filter whilst purification is occurring
        if (this.currentlyFiltering() && this.purifyProgress > 0)
        {
            --this.filterTimeRemaining;
        }

        if (!this.level.isClientSide)
        {
            ItemStack filterStack = this.items.get(1);
            if (this.currentlyFiltering() || !filterStack.isEmpty() && !this.items.get(0).isEmpty())
            {
                IRecipe<?> irecipe = this.level.getRecipeManager().getRecipeFor((IRecipeType<WaterPurifierRecipe>)TANRecipeTypes.WATER_PURIFYING, this, this.level).orElse(null);
                if (!this.currentlyFiltering() && this.canFilter(irecipe))
                {
                    this.filterTimeRemaining = this.getFilterDuration(filterStack);
                    this.filterDuration = this.filterTimeRemaining;

                    // If we are now filtering, consume the filter item
                    if (this.currentlyFiltering())
                    {
                        changed = true;
                        if (filterStack.hasContainerItem())
                            this.items.set(1, filterStack.getContainerItem());
                        else if (!filterStack.isEmpty())
                        {
                            filterStack.shrink(1);

                            // Replace the filter with the containing item (if there is one)
                            // Normally this would be something like an empty bucket
                            if (filterStack.isEmpty())
                            {
                                this.items.set(1, filterStack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.currentlyFiltering() && this.canFilter(irecipe))
                {
                    ++this.purifyProgress;

                    if (this.purifyProgress == this.purifyTotalTime)
                    {
                        this.purifyProgress = 0;
                        this.purifyTotalTime = this.getTotalPurifyTime();
                        this.filter(irecipe);
                        changed = true;
                    }
                }
                else
                {
                    this.purifyProgress = 0;
                }
            }
            else if (!this.currentlyFiltering() && this.purifyProgress > 0)
            {
                this.purifyProgress = MathHelper.clamp(this.purifyProgress - 2, 0, this.purifyTotalTime);
            }

            if (previouslyFiltering != this.currentlyFiltering())
            {
                changed = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(WaterPurifierBlock.PURIFYING, Boolean.valueOf(this.currentlyFiltering())), 3);
            }
        }

        // Mark as changed
        if (changed) this.setChanged();
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new WaterPurifierContainer(id, player, this, this.dataAccess);
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.toughasnails.water_purifier");
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        switch (side)
        {
            case DOWN:
                return SLOTS_FOR_DOWN;
            case UP:
                return SLOTS_FOR_UP;
            default:
                return SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return !(direction == Direction.DOWN && index == 1);
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
        ItemStack currentStack = this.items.get(index);
        boolean sameItem = !stack.isEmpty() && stack.sameItem(currentStack) && ItemStack.tagMatches(stack, currentStack);
        this.items.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        // Update the purify time for the new input
        if (index == 0 && !sameItem)
        {
            this.purifyTotalTime = this.getTotalPurifyTime();
            this.purifyProgress = 0;
            this.setChanged();
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

    public boolean currentlyFiltering()
    {
        return this.filterTimeRemaining > 0;
    }

    protected boolean canFilter(@Nullable IRecipe<?> recipe)
    {
        if (!this.items.get(0).isEmpty() && recipe != null)
        {
            ItemStack recipeResult = recipe.getResultItem();

            // Invalid recipe result
            if (recipeResult.isEmpty())
                return false;

            ItemStack currentResult = this.items.get(2);

            // Result slot is free, we're good to go
            if (currentResult.isEmpty())
                return true;

            // The item can't be filtered if the existing item in the result slot isn't the same as this
            if (!currentResult.sameItem(recipeResult))
                return false;

            if (currentResult.getCount() + recipeResult.getCount() <= this.getMaxStackSize() && currentResult.getCount() + recipeResult.getCount() <= currentResult.getMaxStackSize())
            {
                return true;
            }
            else
            {
                return currentResult.getCount() + recipeResult.getCount() <= recipeResult.getMaxStackSize();
            }
        }
        else return false;
    }

    /** Get the time taken for an input item to be purified. */
    protected int getTotalPurifyTime()
    {
        return this.level.getRecipeManager().getRecipeFor((IRecipeType<WaterPurifierRecipe>) TANRecipeTypes.WATER_PURIFYING, this, this.level).map(WaterPurifierRecipe::getPurifyTime).orElse(200);
    }

    private void filter(@Nullable IRecipe<?> recipe)
    {
        if (recipe != null && this.canFilter(recipe))
        {
            ItemStack input = this.items.get(0);
            ItemStack recipeResult = recipe.getResultItem();
            ItemStack currentResult = this.items.get(2);

            // Update the result stuck
            if (currentResult.isEmpty())
            {
                this.items.set(2, recipeResult.copy());
            }
            else if (currentResult.getItem() == recipeResult.getItem())
            {
                currentResult.grow(recipeResult.getCount());
            }

            // Reduce the input stack
            input.shrink(1);
        }
    }

    public static boolean isFilter(ItemStack stack)
    {
        return getFilterDuration(stack) > 0;
    }

    public static int getFilterDuration(ItemStack filter)
    {
        if (filter.isEmpty())
            return 0;

        return getFilterDurations().get(filter.getItem());
    }

    private static ImmutableMap<Item, Integer> getFilterDurations()
    {
        ImmutableMap.Builder<Item, Integer> builder = ImmutableMap.builder();
        builder.put(Items.CHARCOAL, 1600);
        return builder.build();
    }
}

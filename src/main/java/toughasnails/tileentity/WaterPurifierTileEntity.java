/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.tileentity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.api.tileentity.TANTileEntityTypes;
import toughasnails.block.WaterPurifierBlock;
import toughasnails.container.WaterPurifierContainer;
import toughasnails.core.ToughAsNails;
import toughasnails.crafting.WaterPurifierRecipe;

import javax.annotation.Nullable;

public class WaterPurifierTileEntity extends BaseContainerBlockEntity implements WorldlyContainer, TickableBlockEntity
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

    protected final ContainerData dataAccess = new ContainerData()
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
    public void load(BlockState state, CompoundTag nbt)
    {
        super.load(state, nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.filterTimeRemaining = nbt.getInt("FilterTimeRemaining");
        this.filterDuration = nbt.getInt("FilterDuration");
        this.purifyProgress = nbt.getInt("PurifyProgress");
        this.purifyTotalTime = nbt.getInt("PurifyTotalTime");
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("FilterTimeRemaining", this.filterTimeRemaining);
        nbt.putInt("FilterDuration", this.filterDuration);
        nbt.putInt("PurifyProgress", this.purifyProgress);
        nbt.putInt("PurifyTotalTime", this.purifyTotalTime);
        ContainerHelper.saveAllItems(nbt, this.items);
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
                Recipe<?> irecipe = this.level.getRecipeManager().getRecipeFor((RecipeType<WaterPurifierRecipe>)TANRecipeTypes.WATER_PURIFYING, this, this.level).orElse(null);
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
                this.purifyProgress = Mth.clamp(this.purifyProgress - 2, 0, this.purifyTotalTime);
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
    protected AbstractContainerMenu createMenu(int id, Inventory player)
    {
        return new WaterPurifierContainer(id, player, this, this.dataAccess);
    }

    @Override
    protected Component getDefaultName()
    {
        return new TranslatableComponent("container.toughasnails.water_purifier");
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
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        return ContainerHelper.takeItem(this.items, index);
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
    public boolean stillValid(Player player)
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

    protected boolean canFilter(@Nullable Recipe<?> recipe)
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
        return this.level.getRecipeManager().getRecipeFor((RecipeType<WaterPurifierRecipe>) TANRecipeTypes.WATER_PURIFYING, this, this.level).map(WaterPurifierRecipe::getPurifyTime).orElse(200);
    }

    private void filter(@Nullable Recipe<?> recipe)
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
        if (filter == null || filter.isEmpty())
            return 0;

        return getFilterDurations().getOrDefault(filter.getItem(), 0);
    }

    private static ImmutableMap<Item, Integer> getFilterDurations()
    {
        ImmutableMap.Builder<Item, Integer> builder = ImmutableMap.builder();
        builder.put(Items.CHARCOAL, 1200);
        return builder.build();
    }
}

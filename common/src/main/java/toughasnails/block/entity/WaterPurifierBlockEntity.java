/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.block.WaterPurifierBlock;
import toughasnails.container.WaterPurifierContainer;
import toughasnails.crafting.WaterPurifierRecipe;

import javax.annotation.Nullable;
import java.util.Iterator;

public class WaterPurifierBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer
{
    protected static final int SLOT_INPUT = 0;
    protected static final int SLOT_FILTER = 1;
    protected static final int SLOT_RESULT = 2;
    private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_RESULT, SLOT_FILTER};
    private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_FILTER};

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
                    return WaterPurifierBlockEntity.this.filterTimeRemaining;
                case 1:
                    return WaterPurifierBlockEntity.this.filterDuration;
                case 2:
                    return WaterPurifierBlockEntity.this.purifyProgress;
                case 3:
                    return WaterPurifierBlockEntity.this.purifyTotalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value)
        {
            switch(index) {
                case 0:
                    WaterPurifierBlockEntity.this.filterTimeRemaining = value;
                    break;
                case 1:
                    WaterPurifierBlockEntity.this.filterDuration = value;
                    break;
                case 2:
                    WaterPurifierBlockEntity.this.purifyProgress = value;
                    break;
                case 3:
                    WaterPurifierBlockEntity.this.purifyTotalTime = value;
            }

        }

        @Override
        public int getCount()
        {
            return 4;
        }
    };

    public WaterPurifierBlockEntity(BlockPos pos, BlockState state)
    {
        super(TANBlockEntityTypes.WATER_PURIFIER, pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider lookup)
    {
        super.loadAdditional(nbt, lookup);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items, lookup);
        this.filterTimeRemaining = nbt.getInt("FilterTimeRemaining");
        this.filterDuration = nbt.getInt("FilterDuration");
        this.purifyProgress = nbt.getInt("PurifyProgress");
        this.purifyTotalTime = nbt.getInt("PurifyTotalTime");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        super.saveAdditional(nbt, provider);
        nbt.putInt("FilterTimeRemaining", this.filterTimeRemaining);
        nbt.putInt("FilterDuration", this.filterDuration);
        nbt.putInt("PurifyProgress", this.purifyProgress);
        nbt.putInt("PurifyTotalTime", this.purifyTotalTime);
        ContainerHelper.saveAllItems(nbt, this.items, provider);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WaterPurifierBlockEntity blockEntity)
    {
        boolean previouslyFiltering = blockEntity.isFiltering();
        boolean changed = false;

        if (blockEntity.isFiltering())
        {
            --blockEntity.filterTimeRemaining;
        }

        ItemStack filterStack = blockEntity.items.get(1);
        boolean hasFilter = !filterStack.isEmpty();
        if (blockEntity.isFiltering() || hasFilter && !blockEntity.items.get(0).isEmpty()) {
            RecipeHolder<?> recipe = blockEntity.level.getRecipeManager().getRecipeFor((RecipeType<WaterPurifierRecipe>) TANRecipeTypes.WATER_PURIFYING, new SingleRecipeInput(blockEntity.items.get(0)), blockEntity.level).orElse(null);

            if (recipe != null)
            {
                if (!blockEntity.isFiltering() && blockEntity.canFilter(recipe.value())) {
                    blockEntity.filterTimeRemaining = blockEntity.getFilterDuration(filterStack);
                    blockEntity.filterDuration = blockEntity.filterTimeRemaining;

                    // If we are now filtering, consume the filter item
                    if (blockEntity.isFiltering()) {
                        changed = true;

                        if (hasFilter)
                        {
                            Item filter = filterStack.getItem();
                            filterStack.shrink(1);
                            if (filterStack.isEmpty())
                            {
                                Item remainingItem = filter.getCraftingRemainingItem();
                                blockEntity.items.set(1, remainingItem == null ? ItemStack.EMPTY : new ItemStack(remainingItem));
                            }
                        }
                    }
                }

                if (blockEntity.isFiltering() && blockEntity.canFilter(recipe.value())) {
                    ++blockEntity.purifyProgress;

                    if (blockEntity.purifyProgress == blockEntity.purifyTotalTime) {
                        blockEntity.purifyProgress = 0;
                        blockEntity.purifyTotalTime = blockEntity.getTotalPurifyTime();
                        blockEntity.filter(recipe.value());
                        changed = true;
                    }
                } else {
                    blockEntity.purifyProgress = 0;
                }
            } else if (!blockEntity.isFiltering() && blockEntity.purifyProgress > 0) {
                blockEntity.purifyProgress = Mth.clamp(blockEntity.purifyProgress - 2, 0, blockEntity.purifyTotalTime);
            }
        }

        if (previouslyFiltering != blockEntity.isFiltering())
        {
            changed = true;
            state = state.setValue(WaterPurifierBlock.PURIFYING, blockEntity.isFiltering());
            level.setBlock(pos, state, 3);
        }

        // Mark as changed
        if (changed) setChanged(level, pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player)
    {
        return new WaterPurifierContainer(id, player, this, this.dataAccess);
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.toughasnails.water_purifier");
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
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
    public boolean canPlaceItem(int slot, ItemStack stack)
    {
        if (slot == SLOT_RESULT) return false;
        else if (slot != SLOT_FILTER) return true;
        else
        {
            return isFilter(stack);
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
        return !(direction == Direction.DOWN && index == SLOT_FILTER);
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
        boolean sameItem = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, currentStack);
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

    public boolean isFiltering()
    {
        return this.filterTimeRemaining > 0;
    }

    protected boolean canFilter(@Nullable Recipe<?> recipe)
    {
        if (!this.items.get(0).isEmpty() && recipe != null)
        {
            ItemStack recipeResult = recipe.getResultItem(this.level.registryAccess());

            // Invalid recipe result
            if (recipeResult.isEmpty())
                return false;

            ItemStack currentResult = this.items.get(2);

            // Result slot is free, we're good to go
            if (currentResult.isEmpty())
                return true;

            // The item can't be filtered if the existing item in the result slot isn't the same as this
            if (!ItemStack.isSameItem(currentResult, recipeResult))
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
        return this.level.getRecipeManager().getRecipeFor((RecipeType<WaterPurifierRecipe>) TANRecipeTypes.WATER_PURIFYING, new SingleRecipeInput(this.items.get(0)), this.level).map(r -> r.value().getPurifyTime()).orElse(200);
    }

    private void filter(@Nullable Recipe<?> recipe)
    {
        if (recipe != null && this.canFilter(recipe))
        {
            ItemStack input = this.items.get(0);
            ItemStack recipeResult = recipe.getResultItem(this.level.registryAccess());
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
        add(builder, Items.SHORT_GRASS, 100);
        add(builder, Items.PAPER, 200);
        add(builder, Items.GRAVEL, 400);
        add(builder, ItemTags.SAND, 800);
        add(builder, Items.CHARCOAL, 1600);
        add(builder, Items.PRISMARINE_CRYSTALS, 3200);
        add(builder, Items.HEART_OF_THE_SEA, 6400);
        return builder.build();
    }

    private static void add(ImmutableMap.Builder<Item, Integer> builder, TagKey<Item> tagKey, int i)
    {
        Iterator var3 = BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).iterator();

        while(var3.hasNext())
        {
            Holder<Item> holder = (Holder)var3.next();
            builder.put(holder.value(), i);
        }
    }

    private static void add(ImmutableMap.Builder<Item, Integer> builder, Item item, int i)
    {
        builder.put(item, i);
    }
}

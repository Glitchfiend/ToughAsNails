/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.block.ThermoregulatorBlock;
import toughasnails.container.ThermoregulatorContainer;
import toughasnails.init.ModTags;
import toughasnails.temperature.AreaFill;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ThermoregulatorBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer
{
    /** Radius at which to inform players of a nearby thermoregulator **/
    public static final int INFORM_PLAYER_RADIUS = 20;
    private static final int SPREAD_RADIUS = 16;

    public static final int CONSUMABLE_DURATION = 1600;

    public static final int SLOT_COOLING = 0;
    public static final int SLOT_HEATING = 1;

    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    private int coolingTimeRemaining;
    private int heatingTimeRemaining;
    private int fillTimer = 0;
    private Set<BlockPos> filledBlocks = new HashSet<>();

    protected final ContainerData dataAccess = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            switch(index) {
                case 0:
                    return ThermoregulatorBlockEntity.this.coolingTimeRemaining;
                case 1:
                    return ThermoregulatorBlockEntity.this.heatingTimeRemaining;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value)
        {
            switch(index) {
                case 0:
                    ThermoregulatorBlockEntity.this.coolingTimeRemaining = value;
                    break;
                case 1:
                    ThermoregulatorBlockEntity.this.heatingTimeRemaining = value;
                    break;
            }

        }

        @Override
        public int getCount()
        {
            return 2;
        }
    };


    public ThermoregulatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(TANBlockEntityTypes.THERMOREGULATOR, pos, state);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.coolingTimeRemaining = nbt.getInt("CoolingTimeRemaining");
        this.heatingTimeRemaining = nbt.getInt("HeatingTimeRemaining");
        this.fillTimer = nbt.getInt("FillTimer");

        ListTag list = nbt.getList("FilledBlocks", Tag.TAG_COMPOUND);
        this.filledBlocks = list.stream().map(tag -> NbtUtils.readBlockPos((CompoundTag)tag)).collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
        nbt.putInt("CoolingTimeRemaining", this.coolingTimeRemaining);
        nbt.putInt("HeatingTimeRemaining", this.heatingTimeRemaining);
        nbt.putInt("FillTimer", this.fillTimer);

        ListTag list = new ListTag();
        this.filledBlocks.stream().map(NbtUtils::writeBlockPos).forEach(list::add);
        nbt.put("FilledBlocks", list);

        ContainerHelper.saveAllItems(nbt, this.items);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.saveWithoutMetadata();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ThermoregulatorBlockEntity blockEntity)
    {
        boolean previouslyCooling = blockEntity.isCooling();
        boolean previouslyHeating = blockEntity.isHeating();
        boolean changed = false;

        if (blockEntity.isCooling()) --blockEntity.coolingTimeRemaining;
        if (blockEntity.isHeating()) --blockEntity.heatingTimeRemaining;

        if (!blockEntity.isCooling())
        {
            ItemStack fuel = blockEntity.items.get(SLOT_COOLING);

            if (!fuel.isEmpty())
            {
                Item fuelItem = fuel.getItem();
                blockEntity.coolingTimeRemaining = CONSUMABLE_DURATION;
                changed = true;
                fuel.shrink(1);

                if (fuel.isEmpty())
                {
                    Item remainingItem = fuelItem.getCraftingRemainingItem();
                    blockEntity.items.set(SLOT_COOLING, remainingItem == null ? ItemStack.EMPTY : new ItemStack(remainingItem));
                }
            }
        }

        if (!blockEntity.isHeating())
        {
            ItemStack fuel = blockEntity.items.get(SLOT_HEATING);

            if (!fuel.isEmpty())
            {
                Item fuelItem = fuel.getItem();
                blockEntity.heatingTimeRemaining = CONSUMABLE_DURATION;
                changed = true;
                fuel.shrink(1);

                if (fuel.isEmpty())
                {
                    Item remainingItem = fuelItem.getCraftingRemainingItem();
                    blockEntity.items.set(SLOT_HEATING, remainingItem == null ? ItemStack.EMPTY : new ItemStack(remainingItem));
                }
            }
        }

        if (previouslyCooling != blockEntity.isCooling())
        {
            changed = true;
            state = state.setValue(ThermoregulatorBlock.COOLING, blockEntity.isCooling());
            level.setBlock(pos, state, 3);
        }

        if (previouslyHeating != blockEntity.isHeating())
        {
            changed = true;
            state = state.setValue(ThermoregulatorBlock.HEATING, blockEntity.isHeating());
            level.setBlock(pos, state, 3);
        }

        if (!blockEntity.isHeating() && !blockEntity.isCooling())
        {
            blockEntity.filledBlocks.clear();
        }

        // Fill every second
        if ((blockEntity.isCooling() || blockEntity.isHeating()) && blockEntity.fillTimer % 20 == 0)
        {
            // Update nearby thermoregulators for players
            for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY() - 4, pos.getZ()).inflate(INFORM_PLAYER_RADIUS, (double) INFORM_PLAYER_RADIUS / 2.0, INFORM_PLAYER_RADIUS))) {
                ITemperature temperature = TemperatureHelper.getTemperatureData(player);
                temperature.getNearbyThermoregulators().add(pos);
            }

            // Perform fill
            BlockPos fillStart = pos.relative(state.getValue(ThermoregulatorBlock.FACING));

            blockEntity.filledBlocks.clear();
            AreaFill.fill(level, fillStart, new AreaFill.PositionChecker() {
                @Override
                public void onSolid(Level level, AreaFill.FillPos pos) {}

                @Override
                public void onPassable(Level level, AreaFill.FillPos pos)
                {
                    blockEntity.filledBlocks.add(pos.pos());
                }

                @Override
                public boolean isPassable(Level level, AreaFill.FillPos pos)
                {
                    BlockState state = level.getBlockState(pos.pos());
                    return isConfined(level, pos.pos()) && (state.isAir() || !isFlowBlocking(level, pos, state));
                }
            }, SPREAD_RADIUS);

            changed = true;
        }

        ++blockEntity.fillTimer;

        // Mark as changed
        if (changed)
        {
            setChanged(level, pos, state);
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public Effect getEffectAtPos(BlockPos pos)
    {
        if (this.filledBlocks.contains(pos)) return this.getEffect();
        else return Effect.NONE;
    }

    public ImmutableSet<BlockPos> getFilledBlocks()
    {
        return ImmutableSet.copyOf(this.filledBlocks);
    }

    public Effect getEffect()
    {
        boolean cooling = isCooling();
        boolean heating = isHeating();

        if (cooling && heating) return Effect.NEUTRALIZING;
        else if (cooling) return Effect.COOLING;
        else if (heating) return Effect.HEATING;
        else return Effect.NONE;
    }

    public boolean isCooling()
    {
        return this.coolingTimeRemaining > 0;
    }

    public boolean isHeating()
    {
        return this.heatingTimeRemaining > 0;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player)
    {
        return new ThermoregulatorContainer(id, player, this, this.dataAccess);
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.toughasnails.thermoregulator");
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        if (direction == Direction.UP)
            return new int[]{SLOT_COOLING, SLOT_HEATING};

        BlockState state = this.getBlockState();
        Direction facing = state.getValue(ThermoregulatorBlock.FACING);

        if (facing.getClockWise() == direction) return new int[]{SLOT_COOLING};
        else if (facing.getCounterClockWise() == direction) return new int[]{SLOT_HEATING};
        else return new int[]{};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        if (index == SLOT_COOLING) return isCoolingFuel(stack);
        else if (index == SLOT_HEATING) return isHeatingFuel(stack);
        else return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
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
        boolean sameItem = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, currentStack);
        this.items.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        if (!sameItem)
        {
            this.setChanged();
        }
    }

    @Override
    public void clearContent()
    {
        this.items.clear();
    }

    public static boolean isCoolingFuel(ItemStack stack)
    {
        return stack.is(ModTags.Items.THERMOREGULATOR_COOLING_FUEL);
    }

    public static boolean isHeatingFuel(ItemStack stack)
    {
        return stack.is(ModTags.Items.THERMOREGULATOR_HEATING_FUEL);
    }

    public enum Effect
    {
        COOLING, HEATING, NEUTRALIZING, NONE;
    }
}

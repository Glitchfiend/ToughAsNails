/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.block.entity.ThermoregulatorBlockEntity;
import toughasnails.block.entity.WaterPurifierBlockEntity;

import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public class ThermoregulatorBlock extends BaseEntityBlock
{
    public static final MapCodec<WaterPurifierBlock> CODEC = simpleCodec(WaterPurifierBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty COOLING = BooleanProperty.create("cooling");
    public static final BooleanProperty HEATING = BooleanProperty.create("heating");

    public ThermoregulatorBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COOLING, false).setValue(HEATING, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec()
    {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ThermoregulatorBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return level.isClientSide ? null : createTickerHelper(type, (BlockEntityType<ThermoregulatorBlockEntity>) TANBlockEntityTypes.THERMOREGULATOR, ThermoregulatorBlockEntity::serverTick);
    }

    public static ToIntFunction<BlockState> lightLevel(int level)
    {
        return (state) -> {
            boolean cooling = state.getValue(COOLING);
            boolean heating = state.getValue(HEATING);

            return ((cooling ? 1: 0) + (heating ? 1: 0)) * level;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        return (BlockState)this.defaultBlockState().setValue(FACING, $$0.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return (BlockState)$$0.setValue(FACING, $$1.rotate((Direction)$$0.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return $$0.rotate($$1.getRotation((Direction)$$0.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, COOLING, HEATING);
    }

    public static Effect getEffect(BlockState state)
    {
        boolean heating = state.getValue(HEATING);
        boolean cooling = state.getValue(COOLING);

        if (heating && cooling) return Effect.NEUTRALIZING;
        else if (heating) return Effect.HEATING;
        else return Effect.COOLING;
    }

    public enum Effect
    {
        HEATING,
        COOLING,
        NEUTRALIZING
    }
}

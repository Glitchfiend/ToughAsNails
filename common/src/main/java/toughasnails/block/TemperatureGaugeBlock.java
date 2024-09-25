/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.block.entity.TemperatureGaugeBlockEntity;
import toughasnails.temperature.TemperatureHelperImpl;

import javax.annotation.Nullable;

public class TemperatureGaugeBlock extends BaseEntityBlock
{
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    public TemperatureGaugeBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, Integer.valueOf(0)).setValue(INVERTED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult)
    {
        if (player.mayBuild())
        {
            if (world.isClientSide)
            {
                return InteractionResult.SUCCESS;
            }
            else
            {
                BlockState blockstate = (BlockState)state.cycle(INVERTED);
                world.setBlock(pos, blockstate, 2);
                world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
                 updateSignalStrength(blockstate, world, pos);
                return InteractionResult.CONSUME;
            }
        }
        else
        {
            return super.use(state, world, pos, player, hand, rayTraceResult);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext selectionContext)
    {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction)
    {
        return state.getValue(POWER);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new TemperatureGaugeBlockEntity($$0, $$1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return !level.isClientSide
                ? createTickerHelper(type, (BlockEntityType<TemperatureGaugeBlockEntity>)TANBlockEntityTypes.TEMPERATURE_GAUGE, TemperatureGaugeBlock::tickEntity)
                : null;
    }

    private static void tickEntity(Level level, BlockPos pos, BlockState state, TemperatureGaugeBlockEntity blockEntity)
    {
        if (level.getGameTime() % 20L == 0L) {
            updateSignalStrength(state, level, pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWER, INVERTED);
    }

    private static void updateSignalStrength(BlockState state, Level level, BlockPos pos)
    {
        TemperatureLevel temperatureLevel = TemperatureHelperImpl.getTemperatureAtPosWithoutProximity(level, pos);
        int strength;

        if (state.getValue(INVERTED)) {
            strength = (Mth.clamp(temperatureLevel.ordinal(), 2, 4) - 2) * 8 - 1;
        }
        else
        {
            strength = 15 - Mth.clamp(temperatureLevel.ordinal(), 0, 2) * 8;
        }

        strength = Mth.clamp(strength, 0, 15);
        if (state.getValue(POWER) != strength) {
            level.setBlock(pos, state.setValue(POWER, strength), 3);
        }
    }
}

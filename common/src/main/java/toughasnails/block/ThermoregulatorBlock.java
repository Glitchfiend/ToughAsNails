/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block;

import java.util.function.ToIntFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import toughasnails.api.blockentity.TANBlockEntityTypes;
import toughasnails.api.particle.TANParticles;
import toughasnails.block.entity.ThermoregulatorBlockEntity;

public class ThermoregulatorBlock extends BaseEntityBlock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty COOLING = BooleanProperty.create("cooling");
    public static final BooleanProperty HEATING = BooleanProperty.create("heating");
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public ThermoregulatorBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COOLING, false).setValue(HEATING, false).setValue(ENABLED, true));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if (worldIn.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        else
        {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            return InteractionResult.CONSUME;
        }
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
        return (BlockState)this.defaultBlockState().setValue(FACING, $$0.getHorizontalDirection().getOpposite()).setValue(ENABLED, true);
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
        builder.add(FACING, COOLING, HEATING, ENABLED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean $$4)
    {
        dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, $$4);
    }

    public static void dropContentsOnDestroy(BlockState $$0, BlockState $$1, Level $$2, BlockPos $$3) {
        if (!$$0.is($$1.getBlock())) {
            BlockEntity $$4 = $$2.getBlockEntity($$3);
            if ($$4 instanceof Container) {
                Container $$5 = (Container)$$4;
                Containers.dropContents($$2, $$3, $$5);
                $$2.updateNeighbourForOutputSignal($$3, $$0.getBlock());
            }

        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos $$2, BlockState $$3, boolean $$4)
    {
        if (!$$3.is(state.getBlock())) {
            this.checkPoweredState(level, $$2, state);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
    {
        this.checkPoweredState(level, pos, state);
    }

    private void checkPoweredState(Level level, BlockPos pos, BlockState state)
    {
        boolean flag = !level.hasNeighborSignal(pos);
        if (flag != state.getValue(ENABLED)) {
            level.setBlock(pos, state.setValue(ENABLED, Boolean.valueOf(flag)), 2);
        }

    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public void animateTick(BlockState p_221253_, Level p_221254_, BlockPos p_221255_, RandomSource p_221256_)
    {
        boolean cooling = p_221253_.getValue(COOLING);
        boolean heating = p_221253_.getValue(HEATING);

        Direction direction = p_221253_.getValue(FACING);
        Direction.Axis direction$axis = direction.getAxis();
        BlockPos blockpos = p_221255_.relative(direction);
        BlockState blockstate = p_221254_.getBlockState(blockpos);

        if ((cooling || heating) && !isFaceFull(blockstate.getCollisionShape(p_221254_, blockpos), direction.getOpposite()))
        {
            double d0 = (double)p_221255_.getX() + 0.5D;
            double d1 = (double)p_221255_.getY() + 0.5D;
            double d2 = (double)p_221255_.getZ() + 0.5D;

            double d4 = (p_221256_.nextDouble() * 0.3D) - (p_221256_.nextDouble() * 0.3D);
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.55D : d4;
            double d6 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.55D : d4;

            double ymove = (p_221256_.nextDouble() * 0.05D) - (p_221256_.nextDouble() * 0.05D);
            double xmove = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.15D : ymove;
            double zmove = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.15D : ymove;

            ParticleOptions particle = TANParticles.THERMOREGULATOR_NEUTRAL;
            if (cooling && !heating) { particle = TANParticles.THERMOREGULATOR_COOL; }
            if (!cooling && heating) { particle = TANParticles.THERMOREGULATOR_WARM; }

            p_221254_.addParticle(particle, d0 + d5, d1 + d4, d2 + d6, xmove, ymove, zmove);
        }
    }
}

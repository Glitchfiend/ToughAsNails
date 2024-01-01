/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TemperatureGaugeBlock extends Block
{
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    public TemperatureGaugeBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(INVERTED, false));
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
                // TODO: REPLACE WITH SIGNAL STRENGTH UPDATE
                // updateSignalStrength(blockstate, world, pos);
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
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(INVERTED);
    }
}

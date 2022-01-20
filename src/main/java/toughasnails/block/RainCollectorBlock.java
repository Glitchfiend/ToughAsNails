/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import toughasnails.api.item.TANItems;

public class RainCollectorBlock extends Block
{
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);

    public RainCollectorBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isEmpty() || stack.getItem() != Items.GLASS_BOTTLE)
            return InteractionResult.PASS;

        int waterLevel = state.getValue(LEVEL);

        if (waterLevel > 0 && !worldIn.isClientSide)
        {
            if (!player.getAbilities().instabuild)
            {
                ItemStack newStack = new ItemStack(TANItems.PURIFIED_WATER_BOTTLE);
                player.awardStat(Stats.USE_CAULDRON);
                stack.shrink(1);

                if (stack.isEmpty()) player.setItemInHand(hand, newStack);
                else if (!player.getInventory().add(newStack)) player.drop(newStack, false);
                else player.inventoryMenu.sendAllDataToRemote();
            }

            worldIn.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.setWaterLevel(worldIn, pos, state, waterLevel - 1);
        }

        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    public void setWaterLevel(Level world, BlockPos pos, BlockState state, int level)
    {
        world.setBlock(pos, state.setValue(LEVEL, Integer.valueOf(Mth.clamp(level, 0, 3))), 2);
        world.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation)
    {
        if (level.getBiome(pos).warmEnoughToRain(pos))
        {
            if (state.getValue(LEVEL) < 3)
            {
                level.setBlock(pos, state.cycle(LEVEL), 2);
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos)
    {
        return state.getValue(LEVEL);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type)
    {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(LEVEL);
    }
}

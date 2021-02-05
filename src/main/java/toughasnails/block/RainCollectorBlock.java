/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isEmpty() || stack.getItem() != Items.GLASS_BOTTLE)
            return ActionResultType.PASS;

        int waterLevel = state.getValue(LEVEL);

        if (waterLevel > 0 && !worldIn.isClientSide)
        {
            if (!player.abilities.instabuild)
            {
                ItemStack newStack = new ItemStack(TANItems.PURIFIED_WATER_BOTTLE);
                player.awardStat(Stats.USE_CAULDRON);
                stack.shrink(1);

                if (stack.isEmpty()) player.setItemInHand(hand, newStack);
                else if (!player.inventory.add(newStack)) player.drop(newStack, false);
                else if (player instanceof ServerPlayerEntity) ((ServerPlayerEntity)player).refreshContainer(player.inventoryMenu);
            }

            worldIn.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.setWaterLevel(worldIn, pos, state, waterLevel - 1);
        }

        return ActionResultType.sidedSuccess(worldIn.isClientSide);
    }

    public void setWaterLevel(World world, BlockPos pos, BlockState state, int level)
    {
        world.setBlock(pos, state.setValue(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
        world.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public void handleRain(World worldIn, BlockPos pos)
    {
        float temp = worldIn.getBiome(pos).getTemperature(pos);
        if (!(temp < 0.15F))
        {
            BlockState state = worldIn.getBlockState(pos);
            if (state.getValue(LEVEL) < 3)
            {
                worldIn.setBlock(pos, state.cycle(LEVEL), 2);
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos)
    {
        return state.getValue(LEVEL);
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType type)
    {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(LEVEL);
    }
}

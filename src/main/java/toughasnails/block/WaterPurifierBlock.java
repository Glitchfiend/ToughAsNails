/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import toughasnails.tileentity.WaterPurifierTileEntity;

public class WaterPurifierBlock extends ContainerBlock
{
    public WaterPurifierBlock(Properties builderIn)
    {
        super(builderIn);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (worldIn.isClientSide)
        {
            return ActionResultType.SUCCESS;
        }
        else
        {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn)
    {
        return new WaterPurifierTileEntity();
    }
}

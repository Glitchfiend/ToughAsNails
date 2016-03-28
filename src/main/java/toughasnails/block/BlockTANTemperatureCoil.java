/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.tileentity.TileEntityTemperatureSpread;

public class BlockTANTemperatureCoil extends Block implements ITileEntityProvider
{
    public BlockTANTemperatureCoil() 
    {
        super(Material.rock);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) 
    {
        return new TileEntityTemperatureSpread();
    }
    
    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (TileEntityTemperatureSpread.ENABLE_DEBUG)
        {
            TileEntity te = world.getTileEntity(pos);
            
            if (!world.isRemote && te != null)
            {
                TileEntityTemperatureSpread tempFill = (TileEntityTemperatureSpread)te;
                
                tempFill.reset();
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());

        TileEntity te = worldIn.getTileEntity(pos);
        
        if (!worldIn.isRemote && te != null)
        {
            TileEntityTemperatureSpread tempFill = (TileEntityTemperatureSpread)te;
            
            if (flag)
            {
                tempFill.fill();
            }
            else
            {
                tempFill.reset();
            }
        }
    }

}

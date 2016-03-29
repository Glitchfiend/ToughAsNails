/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.ITANBlock;
import toughasnails.core.ToughAsNails;
import toughasnails.item.ItemTANBlock;
import toughasnails.particle.TANParticleTypes;
import toughasnails.tileentity.TileEntityTemperatureSpread;
import toughasnails.util.BlockStateUtils;

public class BlockTANTemperatureCoil extends Block implements ITANBlock, ITileEntityProvider
{
    public static final PropertyEnum<CoilType> VARIANT = PropertyEnum.create("variant", CoilType.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    protected static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    
    // implement ITANBlock
    @Override
    public Class<? extends ItemBlock> getItemClass() { return ItemTANBlock.class; }
    @Override
    public IProperty[] getPresetProperties() { return new IProperty[] {VARIANT}; }
    @Override
    public IProperty[] getNonRenderingProperties() { return null; }
    @Override
    public String getStateName(IBlockState state)
    {
        return ((CoilType) state.getValue(VARIANT)).getName() + "_coil";
    }
    
    public BlockTANTemperatureCoil() 
    {
        super(Material.iron);
        this.setHardness(1.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, CoilType.COOLING).withProperty(POWERED, Boolean.valueOf(false)));
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDING_BOX;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos)
    {
        return BOUNDING_BOX;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, CoilType.values()[meta & 1]).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
    	if (state.getValue(POWERED) == true)
    	{
	    	if (state.getValue(VARIANT) == CoilType.HEATING)
	    	{
		        double d0 = (double)((float)pos.getX() + 0.4F + rand.nextFloat() * 0.2F);
		        double d1 = (double)((float)pos.getY() + 0.7F + rand.nextFloat() * 0.3F);
		        double d2 = (double)((float)pos.getZ() + 0.4F + rand.nextFloat() * 0.2F);
		        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
	    	}
	    	if (state.getValue(VARIANT) == CoilType.COOLING)
	    	{
		        double d0 = (double)((float)pos.getX() + 0.4F + rand.nextFloat() * 0.2F);
		        double d1 = (double)((float)pos.getY() + 0.7F + rand.nextFloat() * 0.3F);
		        double d2 = (double)((float)pos.getZ() + 0.4F + rand.nextFloat() * 0.2F);
		        ToughAsNails.proxy.spawnParticle(TANParticleTypes.SNOWFLAKE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
	    	}
    	}
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int baseMeta = ((CoilType) state.getValue(VARIANT)).ordinal();
        return baseMeta | (state.getValue(POWERED) ? 8 : 0);
    }

    @Override
    public int getLightValue(IBlockState state)
    {
        return (Boolean)state.getValue(POWERED) ? 7 : 0;
    }
    
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        IBlockState state = this.getStateFromMeta(meta);
        switch (state.getValue(VARIANT))
        {
        case COOLING:
            return new TileEntityTemperatureSpread(-10);
            
        case HEATING:
            return new TileEntityTemperatureSpread(10);
        }
        
        return null;
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
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
        
        super.breakBlock(world, pos, state);
    }
    
    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean flag = world.isBlockPowered(pos) || world.isBlockPowered(pos.up());

        TileEntity te = world.getTileEntity(pos);
        
        if (!world.isRemote && te != null)
        {
            TileEntityTemperatureSpread tempFill = (TileEntityTemperatureSpread)te;
            
            if (flag)
            {
                tempFill.fill();
                world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)));
            }
            else
            {
                tempFill.reset();
                world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
            }
        }
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, POWERED});
    }

    public static enum CoilType implements IStringSerializable
    {
        COOLING, HEATING;
        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
        @Override
        public String toString()
        {
            return this.getName();
        }
    };
}

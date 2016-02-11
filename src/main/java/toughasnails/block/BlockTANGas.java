package toughasnails.block;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.ITANBlock;
import toughasnails.item.ItemTANBlock;

public class BlockTANGas extends Block implements ITANBlock
{
    
    // implement IBOPBlock
    @Override
    public Class<? extends ItemBlock> getItemClass() { return ItemTANBlock.class; }
    @Override
    public int getItemRenderColor(IBlockState state, int tintIndex) { return this.getRenderColor(state); }
    @Override
    public IProperty[] getPresetProperties() { return new IProperty[] {}; }
    @Override
    public IProperty[] getNonRenderingProperties() { return null; }
    @Override
    public String getStateName(IBlockState state) {return "";}

    
    public BlockTANGas() {
        // use rock as default material
        this(Material.air);
    }
    
    public BlockTANGas(Material material)
    {
        super(material);
        // set some defaults
        this.setHardness(0.0F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        // randomly throw up some particles so it looks like the flesh is bubbling
        super.randomDisplayTick(worldIn, pos, state, rand);
        
        if (rand.nextInt(12)==0)
        {           
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
        }
    }
    
    @Override
    public int getRenderType()
    {
        return -1;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }

    @Override
    public boolean isReplaceable(World worldIn, BlockPos pos)
    {
        return true;
    }
    
    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
    	return 2000;
    }
    
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return 2000;
    }
    
}
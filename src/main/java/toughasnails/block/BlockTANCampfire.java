package toughasnails.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.ITANBlock;
import toughasnails.api.TANBlocks;
import toughasnails.item.ItemTANBlock;

public class BlockTANCampfire extends Block implements ITANBlock
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
	public static final PropertyBool BURNING = PropertyBool.create("burning");
    
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

    
    public BlockTANCampfire() {
        // use rock as default material
        this(Material.rock);
    }
    
    public BlockTANCampfire(Material material)
    {
        super(material);
        // set some defaults
        this.setTickRandomly(true);
        this.setHardness(1.0F);
        this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.6F, 0.9F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(BURNING, Boolean.valueOf(false)));
        this.setStepSound(Block.soundTypeStone);
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        int age = ((Integer)state.getValue(AGE)).intValue();

        if (state.getValue(BURNING) == true)
        {
	        if (age < 15)
	        {
	            if (rand.nextInt(10) == 0)
	            {
	                worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(age + 1)), 2);
	            }
	        }
	        if (age == 15)
	        {
	            if (rand.nextInt(10) == 0)
	            {
	                worldIn.setBlockState(pos, state.withProperty(BURNING, false).withProperty(AGE, 0), 2);
	            }
	        }
        }
    }
    
    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
    	IBlockState state = world.getBlockState(pos);
    	
    	if (state.getValue(BURNING) == true)
        {
    		return 15;
        }
    	else
    	{
    		return 0;
    	}
    }
    
    // no collision box - you can walk straight through them
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
    {
    	if (state.getValue(BURNING) == true)
        {
	    	if (entity instanceof EntityLivingBase) {
	            entity.setFire(1); 
	        }
        }
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (playerIn.getCurrentEquippedItem() != null)
        {
            Item item = playerIn.getCurrentEquippedItem().getItem();

            if (state.getValue(BURNING) == false)
            {
	            if (item == Items.stick)
	            {
	            	if (worldIn.rand.nextInt(5) == 0)
	            	{
	            		worldIn.setBlockState(pos, TANBlocks.campfire.getDefaultState().withProperty(BURNING, true));
	            	}
	
	                if (item == Items.stick)
	                {
	                    --playerIn.getCurrentEquippedItem().stackSize;
	                }
	
	                return true;
	            }
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        // randomly throw up some particles so it looks like the flesh is bubbling
        super.randomDisplayTick(worldIn, pos, state, rand);
        
        if (state.getValue(BURNING) == true)
        {
	        worldIn.spawnParticle(EnumParticleTypes.FLAME, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.25F + (rand.nextFloat() / 2.0F)), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
	        worldIn.spawnParticle(EnumParticleTypes.FLAME, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.25F + (rand.nextFloat() / 2.0F)), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
	        worldIn.spawnParticle(EnumParticleTypes.FLAME, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.25F + (rand.nextFloat() / 2.0F)), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
	        
	        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
	        
	        if (rand.nextInt(2)==0)
	        {           
	            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
	        }
        }
    }
    
    // not opaque
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    // not full cube
    @Override
    public boolean isFullCube()
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BURNING, Boolean.valueOf((meta & 1) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Boolean)state.getValue(BURNING)).booleanValue() ? 1 : 0;
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AGE, BURNING});
    }
    
}
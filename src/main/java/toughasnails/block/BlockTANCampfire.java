package toughasnails.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import toughasnails.api.ITANBlock;
import toughasnails.api.TANBlocks;
import toughasnails.item.ItemTANBlock;

public class BlockTANCampfire extends Block implements ITANBlock
{
    protected static final AxisAlignedBB SELECTION_BOX = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.5D, 0.9D);
    
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
	public static final PropertyBool BURNING = PropertyBool.create("burning");
    
    // implement IBOPBlock
    @Override
    public Class<? extends ItemBlock> getItemClass() { return ItemTANBlock.class; }
    @Override
    public IProperty[] getPresetProperties() { return new IProperty[] {}; }
    @Override
    public IProperty[] getNonRenderingProperties() { return new IProperty[] {BURNING}; }
    @Override
    public String getStateName(IBlockState state) {return "";}

    
    public BlockTANCampfire() {
        // use rock as default material
        this(Material.ROCK);
    }
    
    public BlockTANCampfire(Material material)
    {
        super(material);
        // set some defaults
        this.setTickRandomly(true);
        this.setHardness(0.7F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(BURNING, Boolean.valueOf(false)));
        this.setSoundType(SoundType.STONE);
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        int age = ((Integer)state.getValue(AGE)).intValue();

        if (state.getValue(BURNING) == true)
        {
        	if (worldIn.isRainingAt(pos))
        	{
    			worldIn.setBlockState(pos, state.withProperty(BURNING, false).withProperty(AGE, 7), 2);
    	        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
    	        for (int i = 0; i < 8; ++i)
    	        {           
    	            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
    	        }
        	}
            if (rand.nextInt(1) == 0)
            {
                worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(age + 1)), 2);
                if (age + 1 == 7)
                {
                	worldIn.setBlockState(pos, state.withProperty(BURNING, false), 2);
                }
            }
        }
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
    	if (state.getValue(BURNING) == true)
        {
    		return 15;
        }
    	else
    	{
    		return 0;
    	}
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SELECTION_BOX;
    }

    // no collision box - you can walk straight through them
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return NULL_AABB;
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (playerIn.getHeldItem(hand) != null)
        {
            Item item = playerIn.getHeldItem(hand).getItem();
            int age = ((Integer)state.getValue(AGE)).intValue();

            if (age == 0)
            {
	            if (state.getValue(BURNING) == false)
	            {
	            	if (!worldIn.isRainingAt(pos))
	            	{
			            if (item == Items.STICK)
			            {
			            	if (worldIn.rand.nextInt(10) == 0)
			            	{
			            		worldIn.setBlockState(pos, TANBlocks.campfire.getDefaultState().withProperty(BURNING, true));
			            	}
			
			                if (item == Items.STICK)
			                {
                                playerIn.getHeldItem(hand).setCount(playerIn.getHeldItem(hand).getCount() - 1);
			                }
			
			                return true;
			            }
			            
			            if (item == Items.FLINT_AND_STEEL)
			            {
			            	worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
			            	worldIn.setBlockState(pos, TANBlocks.campfire.getDefaultState().withProperty(BURNING, true));
			
			                if (item == Items.FLINT_AND_STEEL)
			                {
			                    playerIn.getHeldItem(hand).damageItem(1, playerIn);
			                }
			
			                return true;
			            }
	            	}
	            }
            } else {
	            if (OreDictionary.getOres("logWood").stream().map(ItemStack::getItem).anyMatch(wood -> wood == item))
	            {
		            playerIn.getHeldItem(hand).setCount(playerIn.getHeldItem(hand).getCount() - 1);
		            worldIn.setBlockState(pos, state.withProperty(AGE, Integer.max(age - 2, 0)), 2);

		            return true;
	            }
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand)
    {
        // randomly throw up some particles so it looks like the flesh is bubbling
        super.randomDisplayTick(state, worldIn, pos, rand);
        
        if (state.getValue(BURNING) == true)
        {
        	if (rand.nextInt(24) == 0)
            {
                worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }
        	
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
    
    @Override
    public int quantityDropped(Random random)
    {
        return 0; //Campfires shouldn't drop anything when mined
    }
    
    // not opaque
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    // not full cube
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, meta >> 1).withProperty(BURNING, Boolean.valueOf((meta & 1) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(AGE) << 1;
        return ((Boolean)state.getValue(BURNING)).booleanValue() ? meta | 1 : meta;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE, BURNING});
    }
    
}

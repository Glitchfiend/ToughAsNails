package toughasnails.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.ITANBlock;
import toughasnails.item.ItemTANBlock;

public class BlockTANGas extends Block implements ITANBlock
{
	
    // add properties
    public static enum GasType implements IStringSerializable
    {
        BLACKDAMP, WHITEDAMP, FIREDAMP, STINKDAMP;
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
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", GasType.class);
    @Override
    protected BlockState createBlockState() {return new BlockState(this, new IProperty[] { VARIANT });}
    
    // implement IBOPBlock
    @Override
    public Class<? extends ItemBlock> getItemClass() { return ItemTANBlock.class; }
    @Override
    public int getItemRenderColor(IBlockState state, int tintIndex) { return this.getRenderColor(state); }
    @Override
    public IProperty[] getPresetProperties() { return new IProperty[] {VARIANT}; }
    @Override
    public IProperty[] getNonRenderingProperties() { return null; }
    @Override
    public String getStateName(IBlockState state)
    {
        return ((GasType) state.getValue(VARIANT)).getName() + "_block";
    }


    public BlockTANGas()
    {
        super(Material.air);
        // set some defaults
        this.setHardness(0.0F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setDefaultState( this.blockState.getBaseState().withProperty(VARIANT, GasType.BLACKDAMP) );
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, GasType.values()[meta]);
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((GasType) state.getValue(VARIANT)).ordinal();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        // randomly throw up some particles so it looks like the flesh is bubbling
        super.randomDisplayTick(worldIn, pos, state, rand);
        
        switch ((GasType) state.getValue(VARIANT))
        {
	        case WHITEDAMP:
	            if (rand.nextInt(12)==0)
	            {           
	                worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
	            }
        
	        case STINKDAMP:
	            if (rand.nextInt(12)==0)
	            {           
	                worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
	            }
        
            case FIREDAMP:
                if (rand.nextInt(12)==0)
                {           
                    worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
                }
                
            case BLACKDAMP: default:
                if (rand.nextInt(12)==0)
                {           
                    worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, (double)((float)pos.getX() + 0.75F - (rand.nextFloat() / 2.0F)), (double)((float)pos.getY() + 0.9F), (double)((float)pos.getZ() + 0.75F - (rand.nextFloat() / 2.0F)), 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(state)});
                }
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
    public boolean canDropFromExplosion(Explosion explosionIn)
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
        IBlockState state = world.getBlockState(pos);
        
        switch ((GasType) state.getValue(VARIANT))
        {
            case FIREDAMP: case STINKDAMP:
                return 2000;
                
            case BLACKDAMP: case WHITEDAMP: default:
                return 0;
        }
    }
    
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        IBlockState state = world.getBlockState(pos);
        
        switch ((GasType) state.getValue(VARIANT))
        {
            case FIREDAMP: case STINKDAMP:
                return 2000;
                
            case BLACKDAMP: case WHITEDAMP: default:
                return 0;
        }
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
    {
    	IBlockState state = worldIn.getBlockState(pos);
    	
        switch ((GasType) state.getValue(VARIANT))
        {
            case STINKDAMP:
            	if (!worldIn.isRemote)
        	    {
        	    	this.explode(worldIn, pos, state);
        	    }
                break;
                
            default:
                break;
        }
    }
    
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        switch ((GasType) state.getValue(VARIANT))
        {
            // suffer wither effect if you walk on deathbloom
            case WHITEDAMP:
                if (entityIn instanceof EntityLivingBase) {
                    ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                }
                break;
            case STINKDAMP:
                if (!worldIn.isRemote && entityIn instanceof EntityArrow)
                {
                    EntityArrow entityarrow = (EntityArrow)entityIn;

                    if (entityarrow.isBurning())
                    {
                    	this.explode(worldIn, pos, state);
                    }
                }
                break;
                
            default:
                break;
        }
    }

    public void explode(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
        	EntityFallingBlock explosion = new EntityFallingBlock(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), state);
        	worldIn.spawnEntityInWorld(explosion);
        	
            float f = 2.0F;
            worldIn.createExplosion(explosion, pos.getX(), pos.getY(), pos.getZ(), f, true);
            
            worldIn.setBlockToAir(pos);
        }
    }
    
}
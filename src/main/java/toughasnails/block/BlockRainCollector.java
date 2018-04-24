package toughasnails.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.ITANBlock;
import toughasnails.api.item.TANItems;
import toughasnails.fluids.PurifiedWaterFluid;
import toughasnails.item.ItemTANBlock;

public class BlockRainCollector extends Block implements ITANBlock
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
    
    // implement ITANBlock
    @Override
    public Class<? extends ItemBlock> getItemClass() { return ItemTANBlock.class; }
    @Override
    public IProperty[] getPresetProperties() { return new IProperty[] {}; }
    @Override
    public IProperty[] getNonRenderingProperties() { return null; }
    @Override
    public String getStateName(IBlockState state) {return "";}

    public BlockRainCollector()
    {
        super(Material.IRON, MapColor.STONE);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
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
    
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (heldItem == null)
        {
            return true;
        }
        else
        {
            int i = ((Integer)state.getValue(LEVEL)).intValue();
            Item item = heldItem.getItem();

            if (item == Items.BUCKET)
            {
                if (i == 3 && !worldIn.isRemote)
                {
                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        playerIn.getHeldItem(hand).setCount(playerIn.getHeldItem(hand).getCount() - 1);

                        if (playerIn.getHeldItem(hand).isEmpty())
                        {
                            playerIn.setHeldItem(hand, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, PurifiedWaterFluid.instance));
                        }
                        else if (!playerIn.inventory.addItemStackToInventory(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, PurifiedWaterFluid.instance)))
                        {
                            playerIn.dropItem(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, PurifiedWaterFluid.instance), false);
                        }
                    }

                    //playerIn.addStat(StatList.CAULDRON_USED);
                    this.setWaterLevel(worldIn, pos, state, 0);
                }

                return true;
            }
            else if (item == Items.GLASS_BOTTLE)
            {
                if (i > 0 && !worldIn.isRemote)
                {
                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        ItemStack itemstack1 = new ItemStack(TANItems.purified_water_bottle, 1);

                        playerIn.getHeldItem(hand).setCount(playerIn.getHeldItem(hand).getCount() - 1);
                        if (playerIn.getHeldItem(hand).isEmpty())
                        {
                            playerIn.setHeldItem(hand, itemstack1);
                        }
                        else if (!playerIn.inventory.addItemStackToInventory(itemstack1))
                        {
                            playerIn.dropItem(itemstack1, false);
                        }
                        else if (playerIn instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                        }
                    }

                    this.setWaterLevel(worldIn, pos, state, i - 1);
                }

                return true;
            }
            else if (item == TANItems.canteen)
            {
            	if (i > 0 && !worldIn.isRemote)
                {
                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        heldItem.setItemDamage(3);
                    }

                    this.setWaterLevel(worldIn, pos, state, i - 1);
                }
            	
            	return true;
            }
            else
            {
            	return false;
            }
        }
    }

    public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level)
    {
        worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
        worldIn.updateComparatorOutputLevel(pos, this);
    }

    @Override
    public void fillWithRain(World worldIn, BlockPos pos)
    {
    	if (worldIn.rand.nextInt(2) == 0)
        {
	        float f = worldIn.getBiome(pos).getTemperature(pos);
	
	        if (worldIn.getBiomeProvider().getTemperatureAtHeight(f, pos.getY()) >= 0.15F)
	        {
	            IBlockState iblockstate = worldIn.getBlockState(pos);
	
	            if (((Integer)iblockstate.getValue(LEVEL)).intValue() < 3)
	            {
	                worldIn.setBlockState(pos, iblockstate.cycleProperty(LEVEL), 2);
	            }
	        }
        }
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return ((Integer)blockState.getValue(LEVEL)).intValue();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(LEVEL)).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {LEVEL});
    }
}
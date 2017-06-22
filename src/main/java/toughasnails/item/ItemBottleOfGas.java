package toughasnails.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.TANBlocks;

public class ItemBottleOfGas extends Item
{
	private Block block;
    
    public enum BottleContents implements IStringSerializable
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
    }

    public ItemBottleOfGas()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        
        this.block = TANBlocks.gas;
    }
    
    // add all the contents types as separate items in the creative tab
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        for (BottleContents contents : BottleContents.values())
        {
            subItems.add(new ItemStack(this, 1, contents.ordinal()));
        }
    }

    // default behavior in Item is to return 0, but the meta value is important here because it determines the jar contents
    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }
    
    public BottleContents getContentsType(ItemStack stack)
    {
        int meta = stack.getMetadata();
        try {
            return BottleContents.values()[meta];
        } catch (Exception e) {
            // if metadata is out of bounds return blackdamp as a default (should never happen)
            return BottleContents.BLACKDAMP;
        }
    }
    
    // get the correct name for this item by looking up the meta value in the JarContents enum
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return  "item.bottle_of_" + this.getContentsType(stack).getName();
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block == Blocks.SNOW_LAYER && ((Integer)iblockstate.getValue(BlockSnow.LAYERS)).intValue() < 1)
        {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }

        if (!playerIn.canPlayerEdit(pos, side, stack))
        {
            return EnumActionResult.PASS;
        }
        else if (stack.isEmpty())
        {
            return EnumActionResult.PASS;
        }
        else
        {
            if (this.block.canPlaceBlockOnSide(worldIn, pos, side))
            {
                IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, side, hitX, hitY, hitZ, this.getMetadata(stack), playerIn);

                if (worldIn.setBlockState(pos, iblockstate1, 3))
                {
                    iblockstate1 = worldIn.getBlockState(pos);

                    if (iblockstate1.getBlock() == this.block)
                    {
                        ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack);
                        iblockstate1.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate1, playerIn, stack);
                    }

                    playerIn.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE, 1, 0));
                    return EnumActionResult.SUCCESS;
                }
            }

            return EnumActionResult.PASS;
        }
    }
}

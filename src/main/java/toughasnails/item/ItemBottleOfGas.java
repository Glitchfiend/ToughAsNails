package toughasnails.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.ITANBlock;
import toughasnails.util.BlockStateUtils;

import com.google.common.collect.ImmutableSet;

public class ItemBottleOfGas extends Item
{
    
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
    }
    
    // add all the contents types as separate items in the creative tab
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        for (BottleContents contents : BottleContents.values())
        {
            subItems.add(new ItemStack(itemIn, 1, contents.ordinal()));
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
}

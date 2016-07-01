package toughasnails.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShards extends Item
{
    
    public enum ShardType implements IStringSerializable
    {
        GLASS, OBSIDIAN;
        
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

    public ItemShards()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    // add all the contents types as separate items in the creative tab
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        for (ShardType contents : ShardType.values())
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
    
    public ShardType getContentsType(ItemStack stack)
    {
        int meta = stack.getMetadata();
        try {
            return ShardType.values()[meta];
        } catch (Exception e) {
            // if metadata is out of bounds return honey as a default (should never happen)
            return ShardType.GLASS;
        }
    }
    
    // get the correct name for this item by looking up the meta value in the JarContents enum
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "_" + this.getContentsType(stack).getName();
    }  
}
   
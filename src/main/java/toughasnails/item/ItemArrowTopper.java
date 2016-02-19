package toughasnails.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemArrowTopper extends Item
{
    
    public static enum TopperType implements IStringSerializable
    {
        FIRE_ARROW_TOPPER, ICE_ARROW_TOPPER, LIGHTNING_ARROW_TOPPER, BOMB_ARROW_TOPPER;
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

        public static TopperType fromMeta(int meta)
        {
            return TopperType.values()[meta % TopperType.values().length];
        }
    };
    
    public ItemArrowTopper()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
   
    // add all the gem types as separate items in the creative tab
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        for (TopperType topperType : TopperType.values())
        {
            subItems.add(new ItemStack(itemIn, 1, topperType.ordinal()));
        }
    }
    
    // default behavior in Item is to return 0, but the meta value is important here because it determines which dart type to use
    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    // get the correct name for this item by looking up the meta value in the DartType enum
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item." + TopperType.fromMeta(stack.getMetadata()).toString();
    }
    
    
}

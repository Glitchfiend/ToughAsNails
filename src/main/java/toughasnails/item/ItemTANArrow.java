package toughasnails.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemTANArrow extends Item
{
    
    public static enum ArrowType implements IStringSerializable
    {
        FIRE_ARROW, ICE_ARROW, BOMB_ARROW;
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
        public float getDamageInflicted()
        {
            switch(this)
            {
                case FIRE_ARROW:
                    return 2.0F;
                default:
                    return 1.0F;
            }
        }
        public static ArrowType fromMeta(int meta)
        {
            return ArrowType.values()[meta % ArrowType.values().length];
        }
    };
    
    public ItemTANArrow()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
   
    // add all the gem types as separate items in the creative tab
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        for (ArrowType arrowType : ArrowType.values())
        {
            subItems.add(new ItemStack(itemIn, 1, arrowType.ordinal()));
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
        return "item." + ArrowType.fromMeta(stack.getMetadata()).toString();
    }
    
    
}

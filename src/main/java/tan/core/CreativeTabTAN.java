package tan.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabTAN extends CreativeTabs
{
    public CreativeTabTAN(int position, String tabID)
    {
        super(position, tabID); 
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(TANItems.thermometer);
    }
}

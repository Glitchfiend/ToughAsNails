package toughasnails.util.inventory;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import toughasnails.api.item.TANItems;

public class CreativeTabTAN extends CreativeTabs
{
    public static final CreativeTabs instance = new CreativeTabTAN(CreativeTabs.getNextID(), "tabToughAsNails");

    private CreativeTabTAN(int index, String label)
    {
        super(index, label);
    }

    @Override
    public Item getTabIconItem()
    {
        return TANItems.tan_icon;
    }
}

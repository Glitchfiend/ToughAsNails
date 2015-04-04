package toughasnails.init;

import static toughasnails.api.TANItems.*;
import toughasnails.item.ItemCanteen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{
    public static void init()
    {
        canteen = registerItem(new ItemCanteen(), "canteen");
    }
    
    public static Item registerItem(Item item, String name)
    {
        item.setUnlocalizedName(name);
        item.setCreativeTab(CreativeTabs.tabMisc);
        
        GameRegistry.registerItem(item, name);
        
        return item;
    }
}

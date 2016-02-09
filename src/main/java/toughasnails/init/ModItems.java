package toughasnails.init;

import static toughasnails.api.TANItems.canteen;
import static toughasnails.api.TANItems.tan_icon;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.item.ItemCanteen;
import toughasnails.util.inventory.CreativeTabTAN;

public class ModItems
{
    public static void init()
    {
        canteen = registerItem(new ItemCanteen(), "canteen");
        tan_icon = registerItem(new Item(), "tan_icon");
        tan_icon.setCreativeTab(null);
    }
    
    public static Item registerItem(Item item, String name)
    {
        item.setUnlocalizedName(name);
        item.setCreativeTab(CreativeTabTAN.instance);
        
        GameRegistry.registerItem(item, name);
        
        return item;
    }
}

package toughasnails.init;

import static toughasnails.api.TANItems.canteen;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.item.ItemCanteen;
import toughasnails.util.inventory.CreativeTabTAN;

public class ModItems
{
    public static void init()
    {
        canteen = registerItem(new ItemCanteen(), "canteen");
    }
    
    public static Item registerItem(Item item, String name)
    {
        item.setUnlocalizedName(name);
        item.setCreativeTab(CreativeTabTAN.instance);
        
        GameRegistry.registerItem(item, name);
        
        return item;
    }
}

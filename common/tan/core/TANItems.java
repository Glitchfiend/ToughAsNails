package tan.core;

import tan.api.ContentRegistry;
import tan.configuration.TANConfigurationIDs;
import tan.items.ItemTANThermometer;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class TANItems
{
    public static Item thermometer;
    
    public static void init()
    {
        initializeItems();
        registerItems();
    }
    
    private static void initializeItems()
    {
        thermometer = new ItemTANThermometer(TANConfigurationIDs.thermometerID).setUnlocalizedName("tan.thermometer");
    }
    
    private static void registerItems()
    {
        registerItem(thermometer);
    }
    
    public static void registerItem(Item item)
    {
        String name = item.getUnlocalizedName().replace("item.", "");
        
        GameRegistry.registerItem(item, name);
        ContentRegistry.addItem(name, item);
    }
}

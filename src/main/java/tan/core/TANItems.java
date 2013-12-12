package tan.core;

import net.minecraft.item.Item;
import tan.api.ContentRegistry;
import tan.configuration.TANConfigurationIDs;
import tan.items.ItemTANCanteen;
import tan.items.ItemTANThermometer;
import cpw.mods.fml.common.registry.GameRegistry;

public class TANItems
{
	public static Item canteen;
    public static Item thermometer;
    
    public static void init()
    {
        initializeItems();
        registerItems();
    }
    
    private static void initializeItems()
    {
    	canteen = new ItemTANCanteen(TANConfigurationIDs.canteenID).setUnlocalizedName("tan.canteen");
        thermometer = new ItemTANThermometer(TANConfigurationIDs.thermometerID).setUnlocalizedName("tan.thermometer");
    }
    
    private static void registerItems()
    {
    	registerItem(canteen);
        registerItem(thermometer);
    }
    
    public static void registerItem(Item item)
    {
        String name = item.getUnlocalizedName().replace("item.", "");
        
        GameRegistry.registerItem(item, name);
        ContentRegistry.addItem(name, item);
    }
}

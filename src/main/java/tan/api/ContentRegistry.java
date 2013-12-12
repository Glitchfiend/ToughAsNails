package tan.api;

import java.util.HashMap;

import net.minecraft.item.Item;

public class ContentRegistry
{
    public static HashMap<String, Item> itemList = new HashMap<String, Item>();
    
    public static void addItem(String name, Item item)
    {
        itemList.put(name, item);
    }
    
    public static Item getItem(String name)
    {
        return itemList.get(name);
    }
}

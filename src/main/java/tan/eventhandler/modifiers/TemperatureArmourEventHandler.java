package tan.eventhandler.modifiers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import tan.api.temperature.TemperatureEvent;
import tan.api.temperature.TemperatureRegistry;

public class TemperatureArmourEventHandler
{
    @ForgeSubscribe
    public void modifyTemperature(TemperatureEvent event)
    {
        EntityPlayer player = event.player;
        
        ItemStack helmetStack = player.inventory.armorInventory[3];
        ItemStack chestplateStack = player.inventory.armorInventory[2];
        ItemStack leggingsStack = player.inventory.armorInventory[1];
        ItemStack bootsStack = player.inventory.armorInventory[0];
        
        float helmetModifier = helmetStack == null ? 0 : TemperatureRegistry.getTemperatureSourceModifier("A", helmetStack.itemID, helmetStack.getItemDamage());
        float chestplateModifier = chestplateStack == null ? 0 : TemperatureRegistry.getTemperatureSourceModifier("A", chestplateStack.itemID, chestplateStack.getItemDamage());
        float leggingsModifier = leggingsStack == null ? 0 : TemperatureRegistry.getTemperatureSourceModifier("A", leggingsStack.itemID, leggingsStack.getItemDamage());
        float bootsModifier = bootsStack == null ? 0 : TemperatureRegistry.getTemperatureSourceModifier("A", bootsStack.itemID, bootsStack.getItemDamage());
        
        event.temperature += (helmetModifier + chestplateModifier + leggingsModifier + bootsModifier);
    }
    
    @ForgeSubscribe
    public void modifyRate(TemperatureEvent.Rate event)
    {
        EntityPlayer player = event.player;
        
        ItemStack helmetStack = player.inventory.armorInventory[3];
        ItemStack chestplateStack = player.inventory.armorInventory[2];
        ItemStack leggingsStack = player.inventory.armorInventory[1];
        ItemStack bootsStack = player.inventory.armorInventory[0];
        
        float helmetModifier = helmetStack == null ? 0 : TemperatureRegistry.getTemperatureSourceRate("A", helmetStack.itemID, helmetStack.getItemDamage());
        float chestplateModifier = chestplateStack == null ? 0 : TemperatureRegistry.getTemperatureSourceRate("A", chestplateStack.itemID, chestplateStack.getItemDamage());
        float leggingsModifier = leggingsStack == null ? 0 : TemperatureRegistry.getTemperatureSourceRate("A", leggingsStack.itemID, leggingsStack.getItemDamage());
        float bootsModifier = bootsStack == null ? 0 : TemperatureRegistry.getTemperatureSourceRate("A", bootsStack.itemID, bootsStack.getItemDamage());
        
        event.rate += (helmetModifier + chestplateModifier + leggingsModifier + bootsModifier);
    }
}

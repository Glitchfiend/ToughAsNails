package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import toughasnails.api.item.TANItems;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;

public class ArmorModifier extends TemperatureModifier
{
    public static final int JELLED_SLIME_TARGET_MODIFIER = -1;
    public static final int WOOL_TARGET_MODIFIER = 1;

	public ArmorModifier(String id)
	{
		super(id);
	}

	@Override
	public Temperature applyPlayerModifiers(EntityPlayer player, Temperature initialTemperature, IModifierMonitor monitor)
    {
        int temperatureLevel = initialTemperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;

        InventoryPlayer inventory = ((EntityPlayer)player).inventory;
        
        //Helmet
        if (inventory.armorInventory.get(3) != ItemStack.EMPTY)
        {
	        if (inventory.armorInventory.get(3).getItem() == TANItems.wool_helmet)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (inventory.armorInventory.get(3).getItem() == TANItems.jelled_slime_helmet)
	        {
	        	newTemperatureLevel += ModConfig.temperature.jelledSlimeArmorModifier;
	        }
        }
        
        //Chestplate
        if (inventory.armorInventory.get(2) != ItemStack.EMPTY)
        {
	        if (inventory.armorInventory.get(2).getItem() == TANItems.wool_chestplate)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (inventory.armorInventory.get(2).getItem() == TANItems.jelled_slime_chestplate)
	        {
	        	newTemperatureLevel += ModConfig.temperature.jelledSlimeArmorModifier;
	        }
        }
        
        //Leggings
        if (inventory.armorInventory.get(1) != ItemStack.EMPTY)
        {
	        if (inventory.armorInventory.get(1).getItem() == TANItems.wool_leggings)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (inventory.armorInventory.get(1).getItem() == TANItems.jelled_slime_leggings)
	        {
	        	newTemperatureLevel += ModConfig.temperature.jelledSlimeArmorModifier;
	        }
        }
        
        //Boots
        if (inventory.armorInventory.get(0) != ItemStack.EMPTY)
        {
	        if (inventory.armorInventory.get(0).getItem() == TANItems.wool_boots)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (inventory.armorInventory.get(0).getItem() == TANItems.jelled_slime_boots)
	        {
	        	newTemperatureLevel += ModConfig.temperature.jelledSlimeArmorModifier;
	        }
        }

		monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Armor", initialTemperature, new Temperature(newTemperatureLevel)));
        
        return new Temperature(newTemperatureLevel);
    }

	@Override
	public boolean isPlayerSpecific()
	{
		return true;
	}
}

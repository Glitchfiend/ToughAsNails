package toughasnails.temperature.modifier;

import glitchcore.inventory.InventoryHelper;
import glitchcore.inventory.InventorySlotType;
import glitchcore.item.StackHelper;
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
        ItemStack helmet = InventoryHelper.getSlot(player, InventorySlotType.ARMOR, 3);
        ItemStack chestplate = InventoryHelper.getSlot(player, InventorySlotType.ARMOR, 2);
        ItemStack leggings = InventoryHelper.getSlot(player, InventorySlotType.ARMOR, 1);
        ItemStack boots = InventoryHelper.getSlot(player, InventorySlotType.ARMOR, 0);

        //Helmet
        if (!StackHelper.isEmpty(helmet))
        {
	        if (helmet.getItem() == TANItems.wool_helmet)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (helmet.getItem() == TANItems.jelled_slime_helmet)
	        {
	        	newTemperatureLevel += ModConfig.temperature.jelledSlimeArmorModifier;
	        }
        }
        
        //Chestplate
        if (!StackHelper.isEmpty(chestplate))
        {
	        if (chestplate.getItem() == TANItems.wool_chestplate)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (chestplate.getItem() == TANItems.jelled_slime_chestplate)
	        {
	        	newTemperatureLevel += ModConfig.temperature.jelledSlimeArmorModifier;
	        }
        }
        
        //Leggings
        if (!StackHelper.isEmpty(leggings))
        {
	        if (leggings.getItem() == TANItems.wool_leggings)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (leggings.getItem() == TANItems.jelled_slime_leggings)
	        {
	        	newTemperatureLevel += ModConfig.temperature.jelledSlimeArmorModifier;
	        }
        }
        
        //Boots
        if (!StackHelper.isEmpty(boots))
        {
	        if (boots.getItem() == TANItems.wool_boots)
	        {
	        	newTemperatureLevel += ModConfig.temperature.woolArmorModifier;
	        }
	        if (boots.getItem() == TANItems.jelled_slime_boots)
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

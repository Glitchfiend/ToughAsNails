package toughasnails.temperature.modifier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.config.json.ArmorTemperatureData;
import toughasnails.init.ModConfig;
import toughasnails.init.ModEnchantments;
import toughasnails.util.config.NBTUtilExt;

import java.util.Map;

public class ArmorModifier extends TemperatureModifier {
    public ArmorModifier(String id) {
        super(id);
    }

    @Override
    public Temperature applyPlayerModifiers(EntityPlayer player, Temperature initialTemperature, IModifierMonitor monitor) {
        int newTemperatureLevel = initialTemperature.getRawValue();

        InventoryPlayer inventory = player.inventory;

        for(int i = 0; i < 4; i++) {
            ItemStack armor = inventory.armorInventory.get(i);
            //Checking armor from config file
            String name = armor.getItem().getRegistryName().toString();
            for(int j = 0; j < ModConfig.armorTemperatureData.size(); j++) {
                ArmorTemperatureData atd = ModConfig.armorTemperatureData.get(j);
                int index = atd.names.indexOf(name);
                if(index >= 0) {
                    NBTTagCompound compare = atd.nbts == null ? null : atd.getNBTTagCompounds().get(index);
                    if(NBTUtilExt.areNBTsEqualOrNull(armor.getTagCompound(), compare)) {
                        newTemperatureLevel += atd.modifier;
                        break;
                    }
                }
            }
            //Checking for enchantments
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(armor);
            if(enchantments.containsKey(ModEnchantments.COOLING)) {
                newTemperatureLevel -= 1;
            } else if(enchantments.containsKey(ModEnchantments.WARMING)) {
                newTemperatureLevel += 1;
            }
        }

        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Armor", initialTemperature, new Temperature(newTemperatureLevel)));
        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific() {
        return true;
    }
}

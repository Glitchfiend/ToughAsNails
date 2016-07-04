package toughasnails.init;

import static toughasnails.api.item.TANItems.canteen;
import static toughasnails.api.item.TANItems.charcoal_filter;
import static toughasnails.api.item.TANItems.freeze_powder;
import static toughasnails.api.item.TANItems.freeze_rod;
import static toughasnails.api.item.TANItems.fruit_juice;
import static toughasnails.api.item.TANItems.ice_charge;
import static toughasnails.api.item.TANItems.ice_cube;
import static toughasnails.api.item.TANItems.jelled_slime;
import static toughasnails.api.item.TANItems.jelled_slime_armor_material;
import static toughasnails.api.item.TANItems.jelled_slime_boots;
import static toughasnails.api.item.TANItems.jelled_slime_chestplate;
import static toughasnails.api.item.TANItems.jelled_slime_helmet;
import static toughasnails.api.item.TANItems.jelled_slime_leggings;
import static toughasnails.api.item.TANItems.lifeblood_crystal;
import static toughasnails.api.item.TANItems.respirator_material;
import static toughasnails.api.item.TANItems.season_clock;
import static toughasnails.api.item.TANItems.spawn_egg;
import static toughasnails.api.item.TANItems.tan_icon;
import static toughasnails.api.item.TANItems.thermometer;
import static toughasnails.api.item.TANItems.water_bottle;
import static toughasnails.api.item.TANItems.wool_armor_material;
import static toughasnails.api.item.TANItems.wool_boots;
import static toughasnails.api.item.TANItems.wool_chestplate;
import static toughasnails.api.item.TANItems.wool_helmet;
import static toughasnails.api.item.TANItems.wool_leggings;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.item.ItemCanteen;
import toughasnails.item.ItemFruitJuice;
import toughasnails.item.ItemIceCharge;
import toughasnails.item.ItemLifebloodCrystal;
import toughasnails.item.ItemSeasonClock;
import toughasnails.item.ItemTANSpawnEgg;
import toughasnails.item.ItemTANWaterBottle;
import toughasnails.item.ItemThermometer;
import toughasnails.util.inventory.CreativeTabTAN;

public class ModItems
{
    public static void init()
    {
    	registerItems();
        //setupModels();
    }
    
    public static void registerItems()
    {
    	// TAN Creative Tab Icon
    	tan_icon = registerItem(new Item(), "tan_icon");
        tan_icon.setCreativeTab(null);
        
        // Armor Materials
        wool_armor_material = EnumHelper.addArmorMaterial("WOOL", "toughasnails:wool_armor", 3, new int[]{2, 2, 2, 1}, 5, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);
        wool_armor_material.customCraftingMaterial = Item.getItemFromBlock(Blocks.WOOL);
        jelled_slime_armor_material = EnumHelper.addArmorMaterial("JELLED_SLIME", "toughasnails:jelled_slime_armor", 9, new int[]{2, 5, 3, 2}, 11, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);
        jelled_slime_armor_material.customCraftingMaterial = TANItems.jelled_slime;
        respirator_material = EnumHelper.addArmorMaterial("RESPIRATOR", "toughasnails:respirator", -1, new int[]{0,0,0,0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);
        
        // Main Items
        thermometer = registerItem(new ItemThermometer(), "thermometer");
        season_clock = registerItem(new ItemSeasonClock(), "season_clock");
        lifeblood_crystal = registerItem(new ItemLifebloodCrystal(), "lifeblood_crystal");
        canteen = registerItem(new ItemCanteen(), "canteen");
        water_bottle = registerItem(new ItemTANWaterBottle(), "water_bottle");
        fruit_juice = registerItem(new ItemFruitJuice(), "fruit_juice");
        
        // Materials
        ice_cube = registerItem(new Item(), "ice_cube");
        freeze_rod = registerItem(new Item(), "freeze_rod");
        freeze_powder = registerItem(new Item(), "freeze_powder");
        ice_charge = registerItem(new ItemIceCharge(), "ice_charge");
        jelled_slime = registerItem(new Item(), "jelled_slime");
        charcoal_filter = registerItem(new Item(), "charcoal_filter");
        //bottle_of_gas = registerItem(new ItemBottleOfGas(), "bottle_of_gas", null);
        
        // Respirators
        //air_filter = registerItem(new Item(), "air_filter", null);
        //respirator = registerItem(new ItemRespirator(respirator_material, 0), "respirator", null);
        
        // Armor
        wool_helmet = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.HEAD), "wool_helmet");
        wool_chestplate = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.CHEST), "wool_chestplate");
        wool_leggings = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.LEGS), "wool_leggings");
        wool_boots = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.FEET), "wool_boots");
        jelled_slime_helmet = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.HEAD), "jelled_slime_helmet");
        jelled_slime_chestplate = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.CHEST), "jelled_slime_chestplate");
        jelled_slime_leggings = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.LEGS), "jelled_slime_leggings");
        jelled_slime_boots = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.FEET), "jelled_slime_boots");
        
        spawn_egg = registerItem(new ItemTANSpawnEgg(), "spawn_egg");
    }
    
    public static Item registerItem(Item item, String name)
    {
        return registerItem(item, name, CreativeTabTAN.instance);
    }
    
    public static Item registerItem(Item item, String name, CreativeTabs tab)
    {    
        item.setUnlocalizedName(name);
        if (tab != null)
        {
            item.setCreativeTab(CreativeTabTAN.instance);
        }
        GameRegistry.register(item, new ResourceLocation(ToughAsNails.MOD_ID, name));
        //TANCommand.itemCount++;
        
        // register sub types if there are any
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            if (item.getHasSubtypes())
            {
                List<ItemStack> subItems = new ArrayList<ItemStack>();
                item.getSubItems(item, CreativeTabTAN.instance, subItems);
                for (ItemStack subItem : subItems)
                {
                    String subItemName = item.getUnlocalizedName(subItem);
                    subItemName =  subItemName.substring(subItemName.indexOf(".") + 1); // remove 'item.' from the front

                    ModelBakery.registerItemVariants(item, new ResourceLocation(ToughAsNails.MOD_ID, subItemName));
                    ModelLoader.setCustomModelResourceLocation(item, subItem.getMetadata(), new ModelResourceLocation(ToughAsNails.MOD_ID + ":" + subItemName, "inventory"));
                }
            }
            else
            {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ToughAsNails.MOD_ID + ":" + name, "inventory"));
            }
        }
        
        return item;   
    }
}

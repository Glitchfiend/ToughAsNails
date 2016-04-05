package toughasnails.init;

import static toughasnails.api.item.TANItems.*;

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
import toughasnails.item.ItemBottleOfGas;
import toughasnails.item.ItemCanteen;
import toughasnails.item.ItemFruitJuice;
import toughasnails.item.ItemIceCharge;
import toughasnails.item.ItemLifebloodCrystal;
import toughasnails.item.ItemRespirator;
import toughasnails.item.ItemSeasonClock;
import toughasnails.item.ItemTANSpawnEgg;
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
    	//TAN Creative Tab Icon
    	tan_icon = registerItem(new Item(), "tan_icon");
        tan_icon.setCreativeTab(null);
        
        //Armor Materials
        wool_armor_material = EnumHelper.addArmorMaterial("WOOL", "toughasnails:wool_armor", 3, new int[]{2, 2, 2, 1}, 5, SoundEvents.item_armor_equip_generic);
        wool_armor_material.customCraftingMaterial = Item.getItemFromBlock(Blocks.wool);
        jelled_slime_armor_material = EnumHelper.addArmorMaterial("JELLED_SLIME", "toughasnails:jelled_slime_armor", 9, new int[]{2, 5, 3, 2}, 11, SoundEvents.item_armor_equip_generic);
        jelled_slime_armor_material.customCraftingMaterial = TANItems.jelled_slime;
        respirator_material = EnumHelper.addArmorMaterial("RESPIRATOR", "toughasnails:respirator", -1, new int[]{0,0,0,0}, 0, SoundEvents.item_armor_equip_generic);
        
        //Main Items
        canteen = registerItem(new ItemCanteen(), "canteen");
        fruit_juice = registerItem(new ItemFruitJuice(), "fruit_juice");
        thermometer = registerItem(new Item(), "thermometer");
        bottle_of_gas = registerItem(new ItemBottleOfGas(), "bottle_of_gas");
        lifeblood_crystal = registerItem(new ItemLifebloodCrystal(), "lifeblood_crystal");
        
        //Wearables
        respirator = registerItem(new ItemRespirator(respirator_material, 0), "respirator");
        wool_helmet = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.HEAD), "wool_helmet");
        wool_chestplate = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.CHEST), "wool_chestplate");
        wool_leggings = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.LEGS), "wool_leggings");
        wool_boots = registerItem(new ItemArmor(wool_armor_material, 0, EntityEquipmentSlot.FEET), "wool_boots");
        jelled_slime_helmet = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.HEAD), "jelled_slime_helmet");
        jelled_slime_chestplate = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.CHEST), "jelled_slime_chestplate");
        jelled_slime_leggings = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.LEGS), "jelled_slime_leggings");
        jelled_slime_boots = registerItem(new ItemArmor(jelled_slime_armor_material, 0, EntityEquipmentSlot.FEET), "jelled_slime_boots");
        
        //Materials
        jelled_slime = registerItem(new Item(), "jelled_slime");
        freeze_rod = registerItem(new Item(), "freeze_rod");
        freeze_powder = registerItem(new Item(), "freeze_powder");
        ice_cube = registerItem(new Item(), "ice_cube");
        ice_charge = registerItem(new ItemIceCharge(), "ice_charge");
        charcoal_filter = registerItem(new Item(), "charcoal_filter");
        air_filter = registerItem(new Item(), "air_filter");
        

        
        season_clock = registerItem(new ItemSeasonClock(), "season_clock");
        
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

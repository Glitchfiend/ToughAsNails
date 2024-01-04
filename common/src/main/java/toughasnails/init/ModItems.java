/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.util.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.item.*;

import java.util.function.BiConsumer;

public class ModItems
{
    public static void registerItems(BiConsumer<ResourceLocation, Item> func)
    {
        // Block Items
        TANItems.TEMPERATURE_GAUGE = register(func, "temperature_gauge", new BlockItem(TANBlocks.TEMPERATURE_GAUGE, new Item.Properties()));
        TANItems.RAIN_COLLECTOR = register(func, "rain_collector", new BlockItem(TANBlocks.RAIN_COLLECTOR, new Item.Properties()));
        TANItems.WATER_PURIFIER = register(func, "water_purifier", new BlockItem(TANBlocks.WATER_PURIFIER, new Item.Properties()));

        // Items
        TANItems.THERMOMETER = register(func, "thermometer", new Item(new Item.Properties().stacksTo(1)));

        TANItems.LEAF_HELMET = register(func, "leaf_helmet", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.HELMET, (new Item.Properties())));
        TANItems.LEAF_CHESTPLATE = register(func, "leaf_chestplate", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
        TANItems.LEAF_LEGGINGS = register(func, "leaf_leggings", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.LEGGINGS, (new Item.Properties())));
        TANItems.LEAF_BOOTS = register(func, "leaf_boots", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.BOOTS, (new Item.Properties())));

        TANItems.WOOL_HELMET = register(func, "wool_helmet", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.HELMET, (new Item.Properties())));
        TANItems.WOOL_CHESTPLATE = register(func, "wool_chestplate", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
        TANItems.WOOL_LEGGINGS = register(func, "wool_leggings", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.LEGGINGS, (new Item.Properties())));
        TANItems.WOOL_BOOTS = register(func, "wool_boots", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.BOOTS, (new Item.Properties())));

        TANItems.ICE_CREAM = register(func, "ice_cream", new StackableBowlFoodItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(0).saturationMod(0.0F).alwaysEat().build())));
        TANItems.CHARC_0S = register(func, "charc_os", new StackableBowlFoodItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(0).saturationMod(0.0F).alwaysEat().build())));

        TANItems.EMPTY_LEATHER_CANTEEN = register(func, "empty_leather_canteen", new EmptyCanteenItem(0, (new Item.Properties()).stacksTo(1)));
        TANItems.LEATHER_DIRTY_WATER_CANTEEN = register(func, "leather_dirty_water_canteen", new FilledCanteenItem(0, (new Item.Properties()).durability(5)));
        TANItems.LEATHER_WATER_CANTEEN = register(func, "leather_water_canteen", new FilledCanteenItem(0, (new Item.Properties()).durability(5)));
        TANItems.LEATHER_PURIFIED_WATER_CANTEEN = register(func, "leather_purified_water_canteen", new FilledCanteenItem(0, (new Item.Properties()).durability(5)));

        TANItems.EMPTY_COPPER_CANTEEN = register(func, "empty_copper_canteen", new EmptyCanteenItem(1, (new Item.Properties()).stacksTo(1)));
        TANItems.COPPER_DIRTY_WATER_CANTEEN = register(func, "copper_dirty_water_canteen", new FilledCanteenItem(1, (new Item.Properties()).durability(6)));
        TANItems.COPPER_WATER_CANTEEN = register(func, "copper_water_canteen", new FilledCanteenItem(1, (new Item.Properties()).durability(6)));
        TANItems.COPPER_PURIFIED_WATER_CANTEEN = register(func, "copper_purified_water_canteen", new FilledCanteenItem(1, (new Item.Properties()).durability(6)));

        TANItems.EMPTY_IRON_CANTEEN = register(func, "empty_iron_canteen", new EmptyCanteenItem(2, (new Item.Properties()).stacksTo(1)));
        TANItems.IRON_DIRTY_WATER_CANTEEN = register(func, "iron_dirty_water_canteen", new FilledCanteenItem(2, (new Item.Properties()).durability(7)));
        TANItems.IRON_WATER_CANTEEN = register(func, "iron_water_canteen", new FilledCanteenItem(2, (new Item.Properties()).durability(7)));
        TANItems.IRON_PURIFIED_WATER_CANTEEN = register(func, "iron_purified_water_canteen", new FilledCanteenItem(2, (new Item.Properties()).durability(7)));

        TANItems.EMPTY_GOLD_CANTEEN = register(func, "empty_gold_canteen", new EmptyCanteenItem(3, (new Item.Properties()).stacksTo(1)));
        TANItems.GOLD_DIRTY_WATER_CANTEEN = register(func, "gold_dirty_water_canteen", new FilledCanteenItem(3, (new Item.Properties()).durability(10)));
        TANItems.GOLD_WATER_CANTEEN = register(func, "gold_water_canteen", new FilledCanteenItem(3, (new Item.Properties()).durability(10)));
        TANItems.GOLD_PURIFIED_WATER_CANTEEN = register(func, "gold_purified_water_canteen", new FilledCanteenItem(3, (new Item.Properties()).durability(10)));

        TANItems.EMPTY_DIAMOND_CANTEEN = register(func, "empty_diamond_canteen", new EmptyCanteenItem(4, (new Item.Properties()).stacksTo(1)));
        TANItems.DIAMOND_DIRTY_WATER_CANTEEN = register(func, "diamond_dirty_water_canteen", new FilledCanteenItem(4, (new Item.Properties()).durability(15)));
        TANItems.DIAMOND_WATER_CANTEEN = register(func, "diamond_water_canteen", new FilledCanteenItem(4, (new Item.Properties()).durability(15)));
        TANItems.DIAMOND_PURIFIED_WATER_CANTEEN = register(func, "diamond_purified_water_canteen", new FilledCanteenItem(4, (new Item.Properties()).durability(15)));

        TANItems.EMPTY_NETHERITE_CANTEEN = register(func, "empty_netherite_canteen", new EmptyCanteenItem(5, (new Item.Properties()).stacksTo(1)));
        TANItems.NETHERITE_DIRTY_WATER_CANTEEN = register(func, "netherite_dirty_water_canteen", new FilledCanteenItem(5, (new Item.Properties()).durability(25)));
        TANItems.NETHERITE_WATER_CANTEEN = register(func, "netherite_water_canteen", new FilledCanteenItem(5, (new Item.Properties()).durability(25)));
        TANItems.NETHERITE_PURIFIED_WATER_CANTEEN = register(func, "netherite_purified_water_canteen", new FilledCanteenItem(5, (new Item.Properties()).durability(25)));

        TANItems.DIRTY_WATER_BOTTLE = register(func, "dirty_water_bottle", new DirtyWaterBottleItem((new Item.Properties()).stacksTo(1)));
        TANItems.PURIFIED_WATER_BOTTLE = register(func, "purified_water_bottle", new PurifiedWaterBottleItem((new Item.Properties()).stacksTo(1)));

        TANItems.APPLE_JUICE = register(func, "apple_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.CACTUS_JUICE = register(func, "cactus_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.CHORUS_FRUIT_JUICE = register(func, "chorus_fruit_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.GLOW_BERRY_JUICE = register(func, "glow_berry_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.MELON_JUICE = register(func, "melon_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.PUMPKIN_JUICE = register(func, "pumpkin_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.SWEET_BERRY_JUICE = register(func, "sweet_berry_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));

        TANItems.TAN_ICON = register(func, "tan_icon", new Item(new Item.Properties()));

        if (Environment.isClient())
        {
            ModClient.registerItemProperties();
        }
    }

    private static Item register(BiConsumer<ResourceLocation, Item> func, String name, Item item)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), item);
        return item;
    }
}

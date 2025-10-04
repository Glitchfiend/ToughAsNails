/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.util.Environment;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.item.*;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ModItems
{
    public static void registerItems(BiConsumer<ResourceLocation, Item> func)
    {
        // Block Items
        TANItems.THERMOREGULATOR = registerBlock(func, TANBlocks.THERMOREGULATOR);
        TANItems.TEMPERATURE_GAUGE = registerBlock(func, TANBlocks.TEMPERATURE_GAUGE);
        TANItems.RAIN_COLLECTOR = registerBlock(func, TANBlocks.RAIN_COLLECTOR);
        TANItems.WATER_PURIFIER = registerBlock(func, TANBlocks.WATER_PURIFIER);

        // Items
        TANItems.THERMOMETER = registerItem(func, "thermometer", Item::new, new Item.Properties().stacksTo(1));

        TANItems.LEAF_HELMET = registerItem(func, "leaf_helmet", new Item.Properties().humanoidArmor(ModArmorMaterials.LEAF, ArmorType.HELMET).stacksTo(1));
        TANItems.LEAF_CHESTPLATE = registerItem(func, "leaf_chestplate", new Item.Properties().humanoidArmor(ModArmorMaterials.LEAF, ArmorType.CHESTPLATE).stacksTo(1));
        TANItems.LEAF_LEGGINGS = registerItem(func, "leaf_leggings", new Item.Properties().humanoidArmor(ModArmorMaterials.LEAF, ArmorType.LEGGINGS).stacksTo(1));
        TANItems.LEAF_BOOTS = registerItem(func, "leaf_boots", new Item.Properties().humanoidArmor(ModArmorMaterials.LEAF, ArmorType.BOOTS).stacksTo(1));

        TANItems.WOOL_HELMET = registerItem(func, "wool_helmet", new Item.Properties().component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF)).humanoidArmor(ModArmorMaterials.WOOL, ArmorType.HELMET).stacksTo(1));
        TANItems.WOOL_CHESTPLATE = registerItem(func, "wool_chestplate", new Item.Properties().component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF)).humanoidArmor(ModArmorMaterials.WOOL, ArmorType.CHESTPLATE).stacksTo(1));
        TANItems.WOOL_LEGGINGS = registerItem(func, "wool_leggings", new Item.Properties().component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF)).humanoidArmor(ModArmorMaterials.WOOL, ArmorType.LEGGINGS).stacksTo(1));
        TANItems.WOOL_BOOTS = registerItem(func, "wool_boots", new Item.Properties().component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF)).humanoidArmor(ModArmorMaterials.WOOL, ArmorType.BOOTS).stacksTo(1));

        TANItems.ICE_CREAM = registerItem(func, "ice_cream", StackableBowlFoodItem::new, new Item.Properties().stacksTo(16).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD).usingConvertsTo(Items.BOWL).food(new FoodProperties.Builder().nutrition(0).saturationModifier(0.0F).alwaysEdible().build()));
        TANItems.CHARC_0S = registerItem(func, "charc_os", StackableBowlFoodItem::new, new Item.Properties().stacksTo(16).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD).usingConvertsTo(Items.BOWL).food(new FoodProperties.Builder().nutrition(0).saturationModifier(0.0F).alwaysEdible().build()));

        TANItems.EMPTY_LEATHER_CANTEEN = registerItem(func, "empty_leather_canteen", p -> new EmptyCanteenItem(0, p), new Item.Properties().enchantable(1).stacksTo(1));
        TANItems.LEATHER_DIRTY_WATER_CANTEEN = registerItem(func, "leather_dirty_water_canteen", p -> new FilledCanteenItem(0, p), new Item.Properties().durability(5));
        TANItems.LEATHER_WATER_CANTEEN = registerItem(func, "leather_water_canteen", p -> new FilledCanteenItem(0, p), new Item.Properties().durability(5));
        TANItems.LEATHER_PURIFIED_WATER_CANTEEN = registerItem(func, "leather_purified_water_canteen", p -> new FilledCanteenItem(0, p), new Item.Properties().durability(5));

        TANItems.EMPTY_COPPER_CANTEEN = registerItem(func, "empty_copper_canteen", p -> new EmptyCanteenItem(1, p), new Item.Properties().enchantable(1).stacksTo(1));
        TANItems.COPPER_DIRTY_WATER_CANTEEN = registerItem(func, "copper_dirty_water_canteen", p -> new FilledCanteenItem(1, p), new Item.Properties().durability(6));
        TANItems.COPPER_WATER_CANTEEN = registerItem(func, "copper_water_canteen", p -> new FilledCanteenItem(1, p), new Item.Properties().durability(6));
        TANItems.COPPER_PURIFIED_WATER_CANTEEN = registerItem(func, "copper_purified_water_canteen", p -> new FilledCanteenItem(1, p), new Item.Properties().durability(6));

        TANItems.EMPTY_IRON_CANTEEN = registerItem(func, "empty_iron_canteen", p -> new EmptyCanteenItem(2, p), new Item.Properties().enchantable(1).stacksTo(1));
        TANItems.IRON_DIRTY_WATER_CANTEEN = registerItem(func, "iron_dirty_water_canteen", p -> new FilledCanteenItem(2, p), new Item.Properties().durability(7));
        TANItems.IRON_WATER_CANTEEN = registerItem(func, "iron_water_canteen", p -> new FilledCanteenItem(2, p), new Item.Properties().durability(7));
        TANItems.IRON_PURIFIED_WATER_CANTEEN = registerItem(func, "iron_purified_water_canteen", p -> new FilledCanteenItem(2, p), new Item.Properties().durability(7));

        TANItems.EMPTY_GOLD_CANTEEN = registerItem(func, "empty_gold_canteen", p -> new EmptyCanteenItem(3, p), new Item.Properties().enchantable(1).stacksTo(1));
        TANItems.GOLD_DIRTY_WATER_CANTEEN = registerItem(func, "gold_dirty_water_canteen", p -> new FilledCanteenItem(3, p), new Item.Properties().durability(10));
        TANItems.GOLD_WATER_CANTEEN = registerItem(func, "gold_water_canteen", p -> new FilledCanteenItem(3, p), new Item.Properties().durability(10));
        TANItems.GOLD_PURIFIED_WATER_CANTEEN = registerItem(func, "gold_purified_water_canteen", p -> new FilledCanteenItem(3, p), new Item.Properties().durability(10));

        TANItems.EMPTY_DIAMOND_CANTEEN = registerItem(func, "empty_diamond_canteen", p -> new EmptyCanteenItem(4, p), new Item.Properties().enchantable(1).stacksTo(1));
        TANItems.DIAMOND_DIRTY_WATER_CANTEEN = registerItem(func, "diamond_dirty_water_canteen", p -> new FilledCanteenItem(4, p), new Item.Properties().durability(15));
        TANItems.DIAMOND_WATER_CANTEEN = registerItem(func, "diamond_water_canteen", p -> new FilledCanteenItem(4, p), new Item.Properties().durability(15));
        TANItems.DIAMOND_PURIFIED_WATER_CANTEEN = registerItem(func, "diamond_purified_water_canteen", p -> new FilledCanteenItem(4, p), new Item.Properties().durability(15));

        TANItems.EMPTY_NETHERITE_CANTEEN = registerItem(func, "empty_netherite_canteen", p -> new EmptyCanteenItem(5, p), new Item.Properties().enchantable(1).stacksTo(1));
        TANItems.NETHERITE_DIRTY_WATER_CANTEEN = registerItem(func, "netherite_dirty_water_canteen", p -> new FilledCanteenItem(5, p), new Item.Properties().durability(25));
        TANItems.NETHERITE_WATER_CANTEEN = registerItem(func, "netherite_water_canteen", p -> new FilledCanteenItem(5, p), new Item.Properties().durability(25));
        TANItems.NETHERITE_PURIFIED_WATER_CANTEEN = registerItem(func, "netherite_purified_water_canteen", p -> new FilledCanteenItem(5, p), new Item.Properties().durability(25));

        TANItems.DIRTY_WATER_BOTTLE = registerItem(func, "dirty_water_bottle", DirtyWaterBottleItem::new, new Item.Properties().stacksTo(1));
        TANItems.PURIFIED_WATER_BOTTLE = registerItem(func, "purified_water_bottle", PurifiedWaterBottleItem::new, new Item.Properties().stacksTo(1));

        TANItems.APPLE_JUICE = registerItem(func, "apple_juice", JuiceItem::new, new Item.Properties().stacksTo(16));
        TANItems.CACTUS_JUICE = registerItem(func, "cactus_juice", JuiceItem::new, new Item.Properties().stacksTo(16));
        TANItems.CHORUS_FRUIT_JUICE = registerItem(func, "chorus_fruit_juice", JuiceItem::new, new Item.Properties().stacksTo(16));
        TANItems.GLOW_BERRY_JUICE = registerItem(func, "glow_berry_juice", JuiceItem::new, new Item.Properties().stacksTo(16));
        TANItems.MELON_JUICE = registerItem(func, "melon_juice", JuiceItem::new, new Item.Properties().stacksTo(16));
        TANItems.PUMPKIN_JUICE = registerItem(func, "pumpkin_juice", JuiceItem::new, new Item.Properties().stacksTo(16));
        TANItems.SWEET_BERRY_JUICE = registerItem(func, "sweet_berry_juice", JuiceItem::new, new Item.Properties().stacksTo(16));

        TANItems.TAN_ICON = registerItem(func, "tan_icon", Item::new, new Item.Properties());
    }

    public static Item registerBlock(BiConsumer<ResourceLocation, Item> func, Block block)
    {
        return registerBlock(func, block, BlockItem::new);
    }

    public static Item registerBlock(BiConsumer<ResourceLocation, Item> func, Block block, BiFunction<Block, Item.Properties, Item> factory)
    {
        return registerBlock(func, block, factory, new Item.Properties());
    }

    public static Item registerBlock(BiConsumer<ResourceLocation, Item> func, Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties properties)
    {
        return registerItem(func, blockIdToItemId(block.builtInRegistryHolder().key()), p_370785_ -> factory.apply(block, p_370785_), properties.useBlockDescriptionPrefix()
        );
    }

    private static Item registerItem(BiConsumer<ResourceLocation, Item> func, ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties)
    {
        Item item = factory.apply(properties.setId(key));
        func.accept(key.location(), item);
        return item;
    }

    private static Item registerItem(BiConsumer<ResourceLocation, Item> func, String name, Function<Item.Properties, Item> factory, Item.Properties properties)
    {
        return registerItem(func, itemId(name), factory, properties);
    }

    private static Item registerItem(BiConsumer<ResourceLocation, Item> func, String name, Item.Properties properties)
    {
        return registerItem(func, itemId(name), Item::new, properties);
    }

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> key)
    {
        return ResourceKey.create(Registries.ITEM, key.location());
    }

    private static ResourceKey<Item> itemId(String name)
    {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, name));
    }
}

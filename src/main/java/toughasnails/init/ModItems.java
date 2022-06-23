/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.item.*;
import toughasnails.util.inventory.ItemGroupTAN;

import java.util.function.Supplier;

public class ModItems
{
    public static void init()
    {
        registerItems();
    }

    private static void registerItems()
    {
        TANItems.EMPTY_CANTEEN = register("empty_canteen", () -> new EmptyCanteenItem((new Item.Properties()).stacksTo(1).tab(ItemGroupTAN.INSTANCE)));
        TANItems.DIRTY_WATER_CANTEEN = register("dirty_water_canteen", () -> new FilledCanteenItem((new Item.Properties()).durability(5).tab(ItemGroupTAN.INSTANCE)));
        TANItems.WATER_CANTEEN = register("water_canteen", () -> new FilledCanteenItem((new Item.Properties()).durability(5).tab(ItemGroupTAN.INSTANCE)));
        TANItems.PURIFIED_WATER_CANTEEN = register("purified_water_canteen", () -> new FilledCanteenItem((new Item.Properties()).durability(5).tab(ItemGroupTAN.INSTANCE)));

        TANItems.DIRTY_WATER_BOTTLE = register("dirty_water_bottle", () -> new DirtyWaterBottleItem((new Item.Properties()).stacksTo(1).tab(ItemGroupTAN.INSTANCE)));
        TANItems.PURIFIED_WATER_BOTTLE = register("purified_water_bottle", () -> new PurifiedWaterBottleItem((new Item.Properties()).stacksTo(1).tab(ItemGroupTAN.INSTANCE)));

        TANItems.APPLE_JUICE = register("apple_juice", () -> new JuiceItem((new Item.Properties()).stacksTo(16).tab(ItemGroupTAN.INSTANCE)));
        TANItems.CACTUS_JUICE = register("cactus_juice", () -> new JuiceItem((new Item.Properties()).stacksTo(16).tab(ItemGroupTAN.INSTANCE)));
        TANItems.CHORUS_FRUIT_JUICE = register("chorus_fruit_juice", () -> new JuiceItem((new Item.Properties()).stacksTo(16).tab(ItemGroupTAN.INSTANCE)));
        TANItems.GLOW_BERRY_JUICE = register("glow_berry_juice", () -> new JuiceItem((new Item.Properties()).stacksTo(16).tab(ItemGroupTAN.INSTANCE)));
        TANItems.MELON_JUICE = register("melon_juice", () -> new JuiceItem((new Item.Properties()).stacksTo(16).tab(ItemGroupTAN.INSTANCE)));
        TANItems.PUMPKIN_JUICE = register("pumpkin_juice", () -> new JuiceItem((new Item.Properties()).stacksTo(16).tab(ItemGroupTAN.INSTANCE)));
        TANItems.SWEET_BERRY_JUICE = register("sweet_berry_juice", () -> new JuiceItem((new Item.Properties()).stacksTo(16).tab(ItemGroupTAN.INSTANCE)));

        TANItems.WOOL_HELMET = register("wool_helmet", () -> new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.HEAD, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        TANItems.WOOL_CHESTPLATE = register("wool_chestplate", () -> new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.CHEST, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        TANItems.WOOL_LEGGINGS = register("wool_leggings", () -> new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.LEGS, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        TANItems.WOOL_BOOTS = register("wool_boots", () -> new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.FEET, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));

        TANItems.LEAF_HELMET = register("leaf_helmet", () -> new LeafArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.HEAD, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        TANItems.LEAF_CHESTPLATE = register("leaf_chestplate", () -> new LeafArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.CHEST, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        TANItems.LEAF_LEGGINGS = register("leaf_leggings", () -> new LeafArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.LEGS, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        TANItems.LEAF_BOOTS = register("leaf_boots", () -> new LeafArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.FEET, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));

        TANItems.TAN_ICON = register("tan_icon", () -> new Item(new Item.Properties()));
    }

    public static RegistryObject<Item> register(String name, Supplier<Item> item)
    {
        return ToughAsNails.ITEM_REGISTER.register(name, item);
    }
}

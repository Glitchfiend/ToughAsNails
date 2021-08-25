/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import cpw.mods.modlauncher.EnumerationHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.item.*;
import toughasnails.util.inventory.ItemGroupTAN;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems
{
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        register("empty_canteen", new EmptyCanteenItem((new Item.Properties()).stacksTo(1).tab(ItemGroupTAN.INSTANCE)));
        register("dirty_water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5).tab(ItemGroupTAN.INSTANCE)));
        register("water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5).tab(ItemGroupTAN.INSTANCE)));
        register("purified_water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5).tab(ItemGroupTAN.INSTANCE)));
        register("dirty_water_bottle", new DirtyWaterBottleItem((new Item.Properties()).stacksTo(1).tab(ItemGroupTAN.INSTANCE)));
        register("purified_water_bottle", new PurifiedWaterBottleItem((new Item.Properties()).stacksTo(1).tab(ItemGroupTAN.INSTANCE)));

        register("wool_helmet", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.HEAD, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        register("wool_chestplate", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.CHEST, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        register("wool_leggings", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.LEGS, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        register("wool_boots", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, EquipmentSlot.FEET, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));

        register("leaf_helmet", new ArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.HEAD, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        register("leaf_chestplate", new ArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.CHEST, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        register("leaf_leggings", new ArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.LEGS, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));
        register("leaf_boots", new ArmorItem(ModArmorMaterials.LEAF, EquipmentSlot.FEET, (new Item.Properties()).tab(ItemGroupTAN.INSTANCE)));

        register("tan_icon", new Item(new Item.Properties()));
    }

    public static void register(String name, Item item)
    {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
    }
}

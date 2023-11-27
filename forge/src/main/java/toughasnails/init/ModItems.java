/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.IRegistryEventContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.item.*;

public class ModItems
{
    public static void registerItems(IRegistryEventContext<Item> context)
    {
        TANItems.EMPTY_CANTEEN = register(context, "empty_canteen", new EmptyCanteenItem((new Item.Properties()).stacksTo(1)));
        TANItems.DIRTY_WATER_CANTEEN = register(context, "dirty_water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5)));
        TANItems.WATER_CANTEEN = register(context, "water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5)));
        TANItems.PURIFIED_WATER_CANTEEN = register(context, "purified_water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5)));

        TANItems.DIRTY_WATER_BOTTLE = register(context, "dirty_water_bottle", new DirtyWaterBottleItem((new Item.Properties()).stacksTo(1)));
        TANItems.PURIFIED_WATER_BOTTLE = register(context, "purified_water_bottle", new PurifiedWaterBottleItem((new Item.Properties()).stacksTo(1)));

        TANItems.APPLE_JUICE = register(context, "apple_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.CACTUS_JUICE = register(context, "cactus_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.CHORUS_FRUIT_JUICE = register(context, "chorus_fruit_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.GLOW_BERRY_JUICE = register(context, "glow_berry_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.MELON_JUICE = register(context, "melon_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.PUMPKIN_JUICE = register(context, "pumpkin_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.SWEET_BERRY_JUICE = register(context, "sweet_berry_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));

        TANItems.WOOL_HELMET = register(context, "wool_helmet", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.HELMET, (new Item.Properties())));
        TANItems.WOOL_CHESTPLATE = register(context, "wool_chestplate", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
        TANItems.WOOL_LEGGINGS = register(context, "wool_leggings", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.LEGGINGS, (new Item.Properties())));
        TANItems.WOOL_BOOTS = register(context, "wool_boots", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.BOOTS, (new Item.Properties())));

        TANItems.LEAF_HELMET = register(context, "leaf_helmet", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.HELMET, (new Item.Properties())));
        TANItems.LEAF_CHESTPLATE = register(context, "leaf_chestplate", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
        TANItems.LEAF_LEGGINGS = register(context, "leaf_leggings", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.LEGGINGS, (new Item.Properties())));
        TANItems.LEAF_BOOTS = register(context, "leaf_boots", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.BOOTS, (new Item.Properties())));

        TANItems.TAN_ICON = register(context, "tan_icon", new Item(new Item.Properties()));

        TANItems.RAIN_COLLECTOR = register(context, "rain_collector", new BlockItem(TANBlocks.RAIN_COLLECTOR, new Item.Properties()));
        TANItems.WATER_PURIFIER = register(context, "water_purifier", new BlockItem(TANBlocks.WATER_PURIFIER, new Item.Properties()));
    }

    private static Item register(IRegistryEventContext<Item> context, String name, Item item)
    {
        return context.register(new ResourceLocation(TANAPI.MOD_ID, name), item);
    }
}

/*******************************************************************************
 * Copyright 2020, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.item.DirtyWaterBottleItem;
import toughasnails.item.FilledCanteenItem;
import toughasnails.item.EmptyCanteenItem;
import toughasnails.item.PurifiedWaterBottleItem;
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
        register("tan_icon", new Item(new Item.Properties()));
    }

    public static void register(String name, Item item)
    {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
    }
}

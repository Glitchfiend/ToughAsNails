/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.client.RegisterColorsEvent;
import glitchcore.util.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.core.ToughAsNails;
import toughasnails.item.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModItems
{
    public static void registerItems(BiConsumer<ResourceLocation, Item> func)
    {
        TANItems.EMPTY_CANTEEN = register(func, "empty_canteen", new EmptyCanteenItem((new Item.Properties()).stacksTo(1)));
        TANItems.DIRTY_WATER_CANTEEN = register(func, "dirty_water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5)));
        TANItems.WATER_CANTEEN = register(func, "water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5)));
        TANItems.PURIFIED_WATER_CANTEEN = register(func, "purified_water_canteen", new FilledCanteenItem((new Item.Properties()).durability(5)));

        TANItems.DIRTY_WATER_BOTTLE = register(func, "dirty_water_bottle", new DirtyWaterBottleItem((new Item.Properties()).stacksTo(1)));
        TANItems.PURIFIED_WATER_BOTTLE = register(func, "purified_water_bottle", new PurifiedWaterBottleItem((new Item.Properties()).stacksTo(1)));

        TANItems.APPLE_JUICE = register(func, "apple_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.CACTUS_JUICE = register(func, "cactus_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.CHORUS_FRUIT_JUICE = register(func, "chorus_fruit_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.GLOW_BERRY_JUICE = register(func, "glow_berry_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.MELON_JUICE = register(func, "melon_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.PUMPKIN_JUICE = register(func, "pumpkin_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));
        TANItems.SWEET_BERRY_JUICE = register(func, "sweet_berry_juice", new JuiceItem((new Item.Properties()).stacksTo(16)));

        TANItems.WOOL_HELMET = register(func, "wool_helmet", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.HELMET, (new Item.Properties())));
        TANItems.WOOL_CHESTPLATE = register(func, "wool_chestplate", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
        TANItems.WOOL_LEGGINGS = register(func, "wool_leggings", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.LEGGINGS, (new Item.Properties())));
        TANItems.WOOL_BOOTS = register(func, "wool_boots", new DyeableWoolArmorItem(ModArmorMaterials.WOOL, ArmorItem.Type.BOOTS, (new Item.Properties())));

        TANItems.LEAF_HELMET = register(func, "leaf_helmet", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.HELMET, (new Item.Properties())));
        TANItems.LEAF_CHESTPLATE = register(func, "leaf_chestplate", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
        TANItems.LEAF_LEGGINGS = register(func, "leaf_leggings", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.LEGGINGS, (new Item.Properties())));
        TANItems.LEAF_BOOTS = register(func, "leaf_boots", new LeafArmorItem(ModArmorMaterials.LEAF, ArmorItem.Type.BOOTS, (new Item.Properties())));

        TANItems.THERMOMETER = register(func, "thermometer", new Item(new Item.Properties().stacksTo(1)));

        TANItems.TAN_ICON = register(func, "tan_icon", new Item(new Item.Properties()));

        TANItems.TEMPERATURE_GAUGE = register(func, "temperature_gauge", new BlockItem(TANBlocks.TEMPERATURE_GAUGE, new Item.Properties()));
        TANItems.RAIN_COLLECTOR = register(func, "rain_collector", new BlockItem(TANBlocks.RAIN_COLLECTOR, new Item.Properties()));
        TANItems.WATER_PURIFIER = register(func, "water_purifier", new BlockItem(TANBlocks.WATER_PURIFIER, new Item.Properties()));

        if (Environment.isClient())
        {
            registerItemProperties();
        }
    }

    public static void registerItemColors(RegisterColorsEvent.Item event)
    {
        event.register((stack, tintIndex) -> {
            return tintIndex > 0 ? -1 : ((DyeableWoolItem)stack.getItem()).getColor(stack);
        }, TANItems.WOOL_HELMET, TANItems.WOOL_CHESTPLATE, TANItems.WOOL_LEGGINGS, TANItems.WOOL_BOOTS);

        event.register((stack, tintIndex) -> {
            return tintIndex > 0 ? -1 : ((LeafArmorItem)stack.getItem()).getColor(stack);
        }, TANItems.LEAF_HELMET, TANItems.LEAF_CHESTPLATE, TANItems.LEAF_LEGGINGS, TANItems.LEAF_BOOTS);
    }

    private static void registerItemProperties()
    {
        ItemProperties.register(TANItems.THERMOMETER, new ResourceLocation(ToughAsNails.MOD_ID, "temperature"), new ClampedItemPropertyFunction() {
            final Map<Integer, Delta> deltas = new HashMap<>();
            private long lastUpdateTick;

            @Override
            public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed)
            {
                Entity holder = entity != null ? entity : stack.getFrame();

                if (holder == null)
                    return 0.5F;

                if (level == null && holder.level() instanceof ClientLevel)
                    level = (ClientLevel)holder.level();

                if (level == null)
                    return 0.5F;

                Delta delta = deltas.computeIfAbsent(holder.getId(), k -> new Delta());

                if (level.getGameTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = level.getGameTime();
                    delta.update(TemperatureHelper.getTemperatureAtPos(level, holder.blockPosition()));
                }

                return delta.getValue();
            }

            private static class Delta
            {
                private double currentValue;
                private double rota;

                private void update(TemperatureLevel temperatureLevel)
                {
                    double targetValue = temperatureLevel.ordinal() * 0.25;
                    double delta = targetValue - this.currentValue;

                    // Add a small increment to the rota to move towards the target value
                    this.rota += delta * 0.1;

                    // Diminish the rota over time. The clock uses 0.9, but we want slightly less wobbling
                    this.rota *= 0.87;
                    this.currentValue = Mth.clamp(this.currentValue + this.rota, 0.0, 1.0);
                }

                public float getValue()
                {
                    // Round to the nearest 0.05
                    return (float)((double)Math.round(this.currentValue * 20.0) / 20.0);
                }
            }
        });
    }

    private static Item register(BiConsumer<ResourceLocation, Item> func, String name, Item item)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), item);
        return item;
    }
}

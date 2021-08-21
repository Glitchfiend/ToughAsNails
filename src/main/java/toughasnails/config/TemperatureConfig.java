/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import com.electronwill.nightconfig.core.Config;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import toughasnails.api.thirst.WaterType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TemperatureConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static ForgeConfigSpec.ConfigValue<List<String>> coolingArmor;
    private static ForgeConfigSpec.ConfigValue<List<String>> warmingArmor;
    private static ForgeConfigSpec.ConfigValue<List<String>> coolingBlocks;
    private static ForgeConfigSpec.ConfigValue<List<String>> warmingBlocks;

    public static ForgeConfigSpec.IntValue nearBlockRange;

    public static ForgeConfigSpec.IntValue nightWarmTemperatureChange;
    public static ForgeConfigSpec.IntValue nightCoolTemperatureChange;

    public static ForgeConfigSpec.IntValue onFireTemperatureChange;
    public static ForgeConfigSpec.IntValue powderSnowTemperatureChange;
    public static ForgeConfigSpec.IntValue wetTemperatureChange;

    private static List<String> defaultCoolingArmor = Lists.newArrayList(
            Items.DIAMOND_BOOTS,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_HELMET
    ).stream().map(item -> item.getRegistryName().toString()).collect(Collectors.toList());

    private static List<String> defaultWarmingArmor = Lists.newArrayList(
            Items.LEATHER_BOOTS,
            Items.LEATHER_LEGGINGS,
            Items.LEATHER_CHESTPLATE,
            Items.LEATHER_HELMET,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_HELMET
    ).stream().map(item -> item.getRegistryName().toString()).collect(Collectors.toList());

    private static final Predicate<Object> RESOURCE_LOC_LIST_VALIDATOR = (obj) ->
    {
        if (!(obj instanceof List)) return false;

        for (Object i : (List)obj)
        {
            if (!(i instanceof String)) return false;

            String s = (String)i;

            // Validate the resource location
            if (ResourceLocation.tryParse(s) == null) return false;
        }

        return true;
    };

    static
    {
        BUILDER.comment("Please be advised that certain temperature-related options are world-specific and are located in <Path to your world folder>/serverconfig/toughasnails-server.toml.");
        BUILDER.push("armor");
        coolingArmor = BUILDER.comment("Armor that cools the player when worn.").define("cooling_armor", defaultCoolingArmor, RESOURCE_LOC_LIST_VALIDATOR);
        warmingArmor = BUILDER.comment("Armor that warms the player when worn.").define("warming_armor", defaultWarmingArmor, RESOURCE_LOC_LIST_VALIDATOR);
        BUILDER.pop();
        BUILDER.push("blocks");
        coolingBlocks = BUILDER.comment("Blocks that cool the player when nearby.").define("cooling_blocks", new ArrayList(), RESOURCE_LOC_LIST_VALIDATOR);
        warmingBlocks = BUILDER.comment("Blocks that warm the player when nearby.").define("warming_blocks", new ArrayList(), RESOURCE_LOC_LIST_VALIDATOR);
        nearBlockRange = BUILDER.comment("The range which constitutes near a heat or cool source").defineInRange("near_block_range", 2, 1, 7);
        BUILDER.pop();
        BUILDER.push("immersion");
        onFireTemperatureChange = BUILDER.comment("Amount to change the temperature by when on fire.").defineInRange("on_fire_temperature_change", 2, -4, 4);
        powderSnowTemperatureChange = BUILDER.comment("Amount to change the temperature by when in powdered snow.").defineInRange("powdered_snow_temperature_change", -2, -4, 4);
        wetTemperatureChange = BUILDER.comment("Amount to change the temperature by when wet.").defineInRange("wet_temperature_change", -1, -4, 4);
        BUILDER.pop();
        BUILDER.push("time");
        nightCoolTemperatureChange = BUILDER.comment("Amount to change the temperature at night when the original temperature is cool or icy.").defineInRange("night_cool_temperature_change", -1, -4, 4);
        nightWarmTemperatureChange = BUILDER.comment("Amount to change the temperature at night when the original temperature is warm or hot.").defineInRange("night_warm_temperature_change", -2, -4, 4);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static boolean isCoolingArmor(Item item)
    {
        return coolingArmor.get().contains(item.getRegistryName().toString());
    }

    public static boolean isWarmingArmor(Item item)
    {
        return warmingArmor.get().contains(item.getRegistryName().toString());
    }

    public static boolean isCoolingBlock(Block block)
    {
        return coolingBlocks.get().contains(block.getRegistryName().toString());
    }

    public static boolean isWarmingBlock(Block block)
    {
        return warmingBlocks.get().contains(block.getRegistryName().toString());
    }
}

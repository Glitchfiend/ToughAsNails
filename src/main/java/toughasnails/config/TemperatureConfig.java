/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TemperatureConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue nearBlockRange;

    public static ForgeConfigSpec.IntValue nightWarmTemperatureChange;
    public static ForgeConfigSpec.IntValue nightCoolTemperatureChange;

    public static ForgeConfigSpec.IntValue onFireTemperatureChange;
    public static ForgeConfigSpec.IntValue powderSnowTemperatureChange;
    public static ForgeConfigSpec.IntValue wetTemperatureChange;

    static
    {
        BUILDER.comment("Please be advised that certain temperature-related options are world-specific and are located in <Path to your world folder>/serverconfig/toughasnails-server.toml.");
        BUILDER.push("blocks");
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
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import toughasnails.api.thirst.WaterType;
import toughasnails.init.ModTags;

public class ThirstConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.DoubleValue thirstExhaustionThreshold;
    public static ForgeConfigSpec.IntValue handDrinkingThirst;
    public static ForgeConfigSpec.DoubleValue handDrinkingHydration;

    static
    {
        BUILDER.comment("Please be advised that certain thirst-related options are world-specific and are located in <Path to your world folder>/serverconfig/toughasnails-server.toml.");
        BUILDER.push("general");
        thirstExhaustionThreshold = BUILDER.comment("The threshold at which exhaustion causes a reduction in hydration and the thirst bar.").defineInRange("exhaustion_threshold", 8.0D, 0.0D, Double.MAX_VALUE);
        BUILDER.pop();

        BUILDER.push("drink_options");
        handDrinkingThirst = BUILDER.comment("Thirst restored from drinking with hands.").defineInRange("hand_drinking_thirst", 1, 0, 20);
        handDrinkingHydration = BUILDER.comment("Hydration restored from drinking with hands.").defineInRange("hand_drinking_hydration", 0.1D, 0.0D, Double.MAX_VALUE);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static WaterType getBiomeWaterType(Holder<Biome> biome)
    {
        if (biome.is(ModTags.Biomes.DIRTY_WATER_BIOMES))
            return WaterType.DIRTY;
        else if (biome.is(ModTags.Biomes.PURIFIED_WATER_BIOMES))
            return WaterType.PURIFIED;
        else
            return WaterType.NORMAL;
    }
}

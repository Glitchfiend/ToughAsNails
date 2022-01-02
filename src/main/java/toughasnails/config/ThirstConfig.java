/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.InMemoryFormat;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.api.thirst.WaterType;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ThirstConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static ForgeConfigSpec.ConfigValue<List<Config>> waterInfoEntries;

    public static ForgeConfigSpec.DoubleValue thirstExhaustionThreshold;
    public static ForgeConfigSpec.IntValue handDrinkingThirst;
    public static ForgeConfigSpec.DoubleValue handDrinkingHydration;

    private static List<Config> defaultWaterInfos = Lists.newArrayList(
        new BiomeWaterInfo(new ResourceLocation("minecraft:swamp"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("minecraft:mushroom_fields"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:bayou"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:bog"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:fungal_jungle"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:highland_moor"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:marsh"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:muskeg"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:ominous_woods"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:volcanic_plains"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:volcano"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:wasteland"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:wetland"), WaterType.DIRTY),
        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:wooded_wasteland"), WaterType.DIRTY),

        new BiomeWaterInfo(new ResourceLocation("biomesoplenty:rainbow_hills"), WaterType.PURIFIED)
    ).stream().map(ThirstConfig::waterInfoToConfig).collect(Collectors.toList());

    private static final Predicate<Object> WATER_INFO_VALIDATOR = (obj) ->
    {
        if (!(obj instanceof List)) return false;

        for (Object i : (List)obj)
        {
            if (!(i instanceof Config)) return false;

            Config config = (Config)i;

            // Ensure config contains required values
            if (!config.contains("location")) return false;
            if (!config.contains("type")) return false;

            // Validate the resource location
            if (ResourceLocation.tryParse(config.get("location")) == null) return false;

            try
            {
                // Validate values
                WaterType type = config.getEnum("type", WaterType.class);
            }
            catch (Exception e)
            {
                return false;
            }
        }

        return true;
    };

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

        BUILDER.push("biome_options");
        waterInfoEntries = BUILDER.comment("The types of water found in biomes from Vanilla and other mods.").define("biome_water_entries", defaultWaterInfos, WATER_INFO_VALIDATOR);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private static Config waterInfoToConfig(BiomeWaterInfo waterInfo)
    {
        Config config = Config.of(LinkedHashMap::new, InMemoryFormat.withUniversalSupport());
        config.add("location", waterInfo.getLocation().toString());
        config.add("type", waterInfo.getType().toString());
        return config;
    }

    private static ImmutableMap<ResourceLocation, BiomeWaterInfo> waterInfoCache;

    public static WaterType getBiomeWaterType(ResourceKey<Biome> key)
    {
        BiomeWaterInfo info = getWaterInfo(key.location());
        return info == null ? WaterType.NORMAL : info.getType();
    }

    @Nullable
    public static BiomeWaterInfo getWaterInfo(ResourceLocation location)
    {
        return getWaterInfos().get(location);
    }

    private static ImmutableMap<ResourceLocation, BiomeWaterInfo> getWaterInfos()
    {
        if (waterInfoCache != null) return waterInfoCache;

        Map<ResourceLocation, BiomeWaterInfo> tmp = Maps.newHashMap();

        for (Config config : waterInfoEntries.get())
        {
            ResourceLocation location = new ResourceLocation(config.get("location"));

            // Skip entries with invalid locations
            if (!ForgeRegistries.BIOMES.containsKey(location))
                continue;

            WaterType type = config.getEnum("type", WaterType.class);
            tmp.put(location, new BiomeWaterInfo(location, type));
        }

        waterInfoCache = ImmutableMap.copyOf(tmp);
        return waterInfoCache;
    }

    public static class BiomeWaterInfo
    {
        private ResourceLocation biomeLocation;
        private WaterType type;

        private BiomeWaterInfo(ResourceLocation location, WaterType type)
        {
            this.biomeLocation = location;
            this.type = type;
        }

        public ResourceLocation getLocation()
        {
            return biomeLocation;
        }

        public WaterType getType()
        {
            return type;
        }
    }
}

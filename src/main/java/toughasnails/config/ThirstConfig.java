/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.InMemoryFormat;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

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

    private static ForgeConfigSpec.ConfigValue<List<Config>> drinkEntries;

    public static ForgeConfigSpec.DoubleValue thirstExhaustionThreshold;

    private static List<Config> defaultDrinks = Lists.newArrayList(
        new DrinkEntry(new ResourceLocation("minecraft:potion"), 4, 0.1F, 0.25F),
        new DrinkEntry(new ResourceLocation("toughasnails:dirty_water_bottle"), 3, 0.25F, 0.75F),
        new DrinkEntry(new ResourceLocation("toughasnails:dirty_water_canteen"), 2, 0.25F, 0.75F),
        new DrinkEntry(new ResourceLocation("toughasnails:purified_water_bottle"), 6, 0.5F, 0.0F),
        new DrinkEntry(new ResourceLocation("toughasnails:purified_water_canteen"), 5, 0.5F, 0.0F),
        new DrinkEntry(new ResourceLocation("toughasnails:water_canteen"), 3, 0.1F, 0.25F))
        .stream().map(ThirstConfig::drinkToConfig).collect(Collectors.toList());

    private static final Predicate<Object> DRINK_VALIDATOR = (obj) ->
    {
        if (!(obj instanceof List)) return false;

        for (Object i : (List)obj)
        {
            if (!(i instanceof Config)) return false;

            Config config = (Config)i;

            // Ensure config contains required values
            if (!config.contains("location")) return false;
            if (!config.contains("thirst")) return false;
            if (!config.contains("hydration")) return false;
            if (!config.contains("poison_chance")) return false;

            // Validate the resource location
            if (ResourceLocation.tryParse(config.get("location")) == null) return false;

            // Validate values
            int thirst = config.getInt("thirst");
            float hydration = config.<Number>get("hydration").floatValue();
            float poisonChance = config.<Number>get("poison_chance").floatValue();

            if (thirst < 0 || thirst > 20) return false;
            if (hydration < 0.0D) return false;
            if (poisonChance < 0.0D || poisonChance > 1.0D) return false;
        }

        return true;
    };

    static
    {
        BUILDER.push("general");
        thirstExhaustionThreshold = BUILDER.comment("The threshold at which exhaustion causes a reduction in hydration and the thirst bar.").defineInRange("exhaustion_threshold", 8.0D, 0.0D, Double.MAX_VALUE);
        BUILDER.pop();

        BUILDER.push("drink_options");
        drinkEntries = BUILDER.comment("Effects of drinks from Vanilla, Tough As Nails and other mods.").define("drink_entries", defaultDrinks, DRINK_VALIDATOR);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private static Config drinkToConfig(DrinkEntry drinkEntry)
    {
        Config config = Config.of(LinkedHashMap::new, InMemoryFormat.withUniversalSupport());
        config.add("location", drinkEntry.getLocation().toString());
        config.add("thirst", drinkEntry.getThirst());
        config.add("hydration", drinkEntry.getHydration());
        config.add("poison_chance", drinkEntry.getPoisonChance());
        return config;
    }

    private static ImmutableMap<ResourceLocation, DrinkEntry> drinkEntryCache;

    @Nullable
    public static DrinkEntry getDrinkEntry(ResourceLocation location)
    {
        return getDrinkEntries().get(location);
    }

    private static ImmutableMap<ResourceLocation, DrinkEntry> getDrinkEntries()
    {
        if (drinkEntryCache != null) return drinkEntryCache;

        Map<ResourceLocation, DrinkEntry> tmp = Maps.newHashMap();

        for (Config config : drinkEntries.get())
        {
            ResourceLocation location = new ResourceLocation(config.get("location"));

            // Skip entries with invalid locations
            if (!ForgeRegistries.ITEMS.containsKey(location))
                continue;

            float hydration = config.<Number>get("hydration").floatValue();
            float poisonChance = config.<Number>get("poison_chance").floatValue();

            tmp.put(location, new DrinkEntry(location, config.getInt("thirst"), hydration, poisonChance));
        }

        drinkEntryCache = ImmutableMap.copyOf(tmp);
        return drinkEntryCache;
    }

    public static class DrinkEntry
    {
        private ResourceLocation location;
        private int thirst;
        private float hydration;
        private float poisonChance;

        public DrinkEntry(ResourceLocation location, int thirst, float hydration, float poisonChance)
        {
            this.location = location;
            this.thirst = thirst;
            this.hydration = hydration;
            this.poisonChance = poisonChance;
        }

        public ResourceLocation getLocation()
        {
            return location;
        }

        public int getThirst()
        {
            return thirst;
        }

        public float getHydration()
        {
            return hydration;
        }

        public float getPoisonChance()
        {
            return poisonChance;
        }
    }
}

package toughasnails.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import toughasnails.api.TANAPI;

public class ModTags
{
    public static void init()
    {
        Blocks.init();
        Items.init();
        Biomes.init();
    }

    public static class Blocks
    {
        private static void init() {}

        public static final TagKey<Block> COOLING_BLOCKS = create(new ResourceLocation(TANAPI.MOD_ID, "cooling_blocks"));
        public static final TagKey<Block> HEATING_BLOCKS = create(new ResourceLocation(TANAPI.MOD_ID, "heating_blocks"));

        public static TagKey<Block> create(ResourceLocation name) 
        {
            return TagKey.create(Registries.BLOCK, name);
        }
    }

    public static class Items
    {
        private static void init() {}

        public static final TagKey<Item> COOLING_ARMOR = create(new ResourceLocation(TANAPI.MOD_ID, "cooling_armor"));
        public static final TagKey<Item> HEATING_ARMOR = create(new ResourceLocation(TANAPI.MOD_ID, "heating_armor"));
        public static final TagKey<Item> COOLING_ITEMS = create(new ResourceLocation(TANAPI.MOD_ID, "cooling_items"));
        public static final TagKey<Item> HEATING_ITEMS = create(new ResourceLocation(TANAPI.MOD_ID, "heating_items"));

        public static final TagKey<Item> ONE_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/1_thirst_drinks"));
        public static final TagKey<Item> TWO_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/2_thirst_drinks"));
        public static final TagKey<Item> THREE_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/3_thirst_drinks"));
        public static final TagKey<Item> FOUR_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/4_thirst_drinks"));
        public static final TagKey<Item> FIVE_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/5_thirst_drinks"));
        public static final TagKey<Item> SIX_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/6_thirst_drinks"));
        public static final TagKey<Item> SEVEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/7_thirst_drinks"));
        public static final TagKey<Item> EIGHT_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/8_thirst_drinks"));
        public static final TagKey<Item> NINE_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/9_thirst_drinks"));
        public static final TagKey<Item> TEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/10_thirst_drinks"));
        public static final TagKey<Item> ELEVEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/11_thirst_drinks"));
        public static final TagKey<Item> TWELVE_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/12_thirst_drinks"));
        public static final TagKey<Item> THIRTEEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/13_thirst_drinks"));
        public static final TagKey<Item> FOURTEEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/14_thirst_drinks"));
        public static final TagKey<Item> FIFTEEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/15_thirst_drinks"));
        public static final TagKey<Item> SIXTEEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/16_thirst_drinks"));
        public static final TagKey<Item> SEVENTEEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/17_thirst_drinks"));
        public static final TagKey<Item> EIGHTEEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/18_thirst_drinks"));
        public static final TagKey<Item> NINETEEN_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/19_thirst_drinks"));
        public static final TagKey<Item> TWENTY_THIRST_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "thirst/20_thirst_drinks"));

        public static final TagKey<Item> TEN_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/10_hydration_drinks"));
        public static final TagKey<Item> TWENTY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/20_hydration_drinks"));
        public static final TagKey<Item> THIRTY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/30_hydration_drinks"));
        public static final TagKey<Item> FOURTY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/40_hydration_drinks"));
        public static final TagKey<Item> FIFTY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/50_hydration_drinks"));
        public static final TagKey<Item> SIXTY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/60_hydration_drinks"));
        public static final TagKey<Item> SEVENTY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/70_hydration_drinks"));
        public static final TagKey<Item> EIGHTY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/80_hydration_drinks"));
        public static final TagKey<Item> NINETY_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/90_hydration_drinks"));
        public static final TagKey<Item> ONE_HUNDRED_HYDRATION_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "hydration/100_hydration_drinks"));

        public static final TagKey<Item> TWENTY_FIVE_POISON_CHANCE_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "poison_chance/25_poison_chance_drinks"));
        public static final TagKey<Item> FIFTY_POISON_CHANCE_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "poison_chance/50_poison_chance_drinks"));
        public static final TagKey<Item> SEVENTY_FIVE_POISON_CHANCE_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "poison_chance/75_poison_chance_drinks"));
        public static final TagKey<Item> ONE_HUNDRED_POISON_CHANCE_DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "poison_chance/100_poison_chance_drinks"));

        public static final TagKey<Item> DRINKS = create(new ResourceLocation(TANAPI.MOD_ID, "drinks"));
        
        public static int getThirstRestored(ItemStack drink)
        {
            if (drink.is(ModTags.Items.ONE_THIRST_DRINKS)) { return 1; }
            if (drink.is(ModTags.Items.TWO_THIRST_DRINKS)) { return 2; }
            if (drink.is(ModTags.Items.THREE_THIRST_DRINKS)) { return 3; }
            if (drink.is(ModTags.Items.FOUR_THIRST_DRINKS)) { return 4; }
            if (drink.is(ModTags.Items.FIVE_THIRST_DRINKS)) { return 5; }
            if (drink.is(ModTags.Items.SIX_THIRST_DRINKS)) { return 6; }
            if (drink.is(ModTags.Items.SEVEN_THIRST_DRINKS)) { return 7; }
            if (drink.is(ModTags.Items.EIGHT_THIRST_DRINKS)) { return 8; }
            if (drink.is(ModTags.Items.NINE_THIRST_DRINKS)) { return 9; }
            if (drink.is(ModTags.Items.TEN_THIRST_DRINKS)) { return 10; }
            if (drink.is(ModTags.Items.ELEVEN_THIRST_DRINKS)) { return 11; }
            if (drink.is(ModTags.Items.TWELVE_THIRST_DRINKS)) { return 12; }
            if (drink.is(ModTags.Items.THIRTEEN_THIRST_DRINKS)) { return 13; }
            if (drink.is(ModTags.Items.FOURTEEN_THIRST_DRINKS)) { return 14; }
            if (drink.is(ModTags.Items.FIFTEEN_THIRST_DRINKS)) { return 15; }
            if (drink.is(ModTags.Items.SIXTEEN_THIRST_DRINKS)) { return 16; }
            if (drink.is(ModTags.Items.SEVENTEEN_THIRST_DRINKS)) { return 17; }
            if (drink.is(ModTags.Items.EIGHTEEN_THIRST_DRINKS)) { return 18; }
            if (drink.is(ModTags.Items.NINETEEN_THIRST_DRINKS)) { return 19; }
            if (drink.is(ModTags.Items.TWENTY_THIRST_DRINKS)) { return 20; }
            return 0;
        }

        public static TagKey<Item> create(final ResourceLocation name) 
        {
            return TagKey.create(Registries.ITEM, name);
        }
    }

    public static class Biomes
    {
        private static void init() {}

        public static final TagKey<Biome> ICY_BIOMES = create(new ResourceLocation(TANAPI.MOD_ID, "icy_biomes"));
        public static final TagKey<Biome> COLD_BIOMES = create(new ResourceLocation(TANAPI.MOD_ID, "cold_biomes"));
        public static final TagKey<Biome> NEUTRAL_BIOMES = create(new ResourceLocation(TANAPI.MOD_ID, "neutral_biomes"));
        public static final TagKey<Biome> WARM_BIOMES = create(new ResourceLocation(TANAPI.MOD_ID, "warm_biomes"));
        public static final TagKey<Biome> HOT_BIOMES = create(new ResourceLocation(TANAPI.MOD_ID, "hot_biomes"));
        public static final TagKey<Biome> DIRTY_WATER_BIOMES = create(new ResourceLocation(TANAPI.MOD_ID, "dirty_water_biomes"));
        public static final TagKey<Biome> PURIFIED_WATER_BIOMES = create(new ResourceLocation(TANAPI.MOD_ID, "purified_water_biomes"));

        private static TagKey<Biome> create(ResourceLocation loc)
        {
            return TagKey.create(Registries.BIOME, loc);
        }
    }
}

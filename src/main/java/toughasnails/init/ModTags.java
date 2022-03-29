package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import toughasnails.core.ToughAsNails;

public class ModTags
{
    public static void init()
    {
        Blocks.init();
        Items.init();
    }

    public static class Blocks
    {
        private static void init() {}

        public static final TagKey<Block> COOLING_BLOCKS = BlockTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "cooling_blocks"));
        public static final TagKey<Block> HEATING_BLOCKS = BlockTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "heating_blocks"));
    }

    public static class Items
    {
        private static void init() {}

        public static final TagKey<Item> COOLING_ARMOR = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "cooling_armor"));
        public static final TagKey<Item> HEATING_ARMOR = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "heating_armor"));
        public static final TagKey<Item> COOLING_ITEMS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "cooling_items"));
        public static final TagKey<Item> HEATING_ITEMS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "heating_items"));

        public static final TagKey<Item> ONE_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/1_thirst_drinks"));
        public static final TagKey<Item> TWO_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/2_thirst_drinks"));
        public static final TagKey<Item> THREE_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/3_thirst_drinks"));
        public static final TagKey<Item> FOUR_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/4_thirst_drinks"));
        public static final TagKey<Item> FIVE_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/5_thirst_drinks"));
        public static final TagKey<Item> SIX_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/6_thirst_drinks"));
        public static final TagKey<Item> SEVEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/7_thirst_drinks"));
        public static final TagKey<Item> EIGHT_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/8_thirst_drinks"));
        public static final TagKey<Item> NINE_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/9_thirst_drinks"));
        public static final TagKey<Item> TEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/10_thirst_drinks"));
        public static final TagKey<Item> ELEVEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/11_thirst_drinks"));
        public static final TagKey<Item> TWELVE_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/12_thirst_drinks"));
        public static final TagKey<Item> THIRTEEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/13_thirst_drinks"));
        public static final TagKey<Item> FOURTEEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/14_thirst_drinks"));
        public static final TagKey<Item> FIFTEEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/15_thirst_drinks"));
        public static final TagKey<Item> SIXTEEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/16_thirst_drinks"));
        public static final TagKey<Item> SEVENTEEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/17_thirst_drinks"));
        public static final TagKey<Item> EIGHTEEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/18_thirst_drinks"));
        public static final TagKey<Item> NINETEEN_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/19_thirst_drinks"));
        public static final TagKey<Item> TWENTY_THIRST_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "thirst/20_thirst_drinks"));

        public static final TagKey<Item> TEN_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/10_hydration_drinks"));
        public static final TagKey<Item> TWENTY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/20_hydration_drinks"));
        public static final TagKey<Item> THIRTY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/30_hydration_drinks"));
        public static final TagKey<Item> FOURTY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/40_hydration_drinks"));
        public static final TagKey<Item> FIFTY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/50_hydration_drinks"));
        public static final TagKey<Item> SIXTY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/60_hydration_drinks"));
        public static final TagKey<Item> SEVENTY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/70_hydration_drinks"));
        public static final TagKey<Item> EIGHTY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/80_hydration_drinks"));
        public static final TagKey<Item> NINETY_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/90_hydration_drinks"));
        public static final TagKey<Item> ONE_HUNDRED_HYDRATION_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "hydration/100_hydration_drinks"));

        public static final TagKey<Item> TWENTY_FIVE_POISON_CHANCE_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "poison_chance/25_poison_chance_drinks"));
        public static final TagKey<Item> FIFTY_POISON_CHANCE_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "poison_chance/50_poison_chance_drinks"));
        public static final TagKey<Item> SEVENTY_FIVE_POISON_CHANCE_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "poison_chance/75_poison_chance_drinks"));
        public static final TagKey<Item> ONE_HUNDRED_POISON_CHANCE_DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "poison_chance/100_poison_chance_drinks"));

        public static final TagKey<Item> DRINKS = ItemTags.create(new ResourceLocation(ToughAsNails.MOD_ID, "drinks"));
    }
}

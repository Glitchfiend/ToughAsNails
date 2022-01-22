package toughasnails.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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

        public static final Tag.Named<Block> COOLING_BLOCKS = BlockTags.bind("toughasnails:cooling_blocks");
        public static final Tag.Named<Block> HEATING_BLOCKS = BlockTags.bind("toughasnails:heating_blocks");
    }

    public static class Items
    {
        private static void init() {}

        public static final Tag.Named<Item> COOLING_ARMOR = ItemTags.bind("toughasnails:cooling_armor");
        public static final Tag.Named<Item> HEATING_ARMOR = ItemTags.bind("toughasnails:heating_armor");
        public static final Tag.Named<Item> COOLING_ITEMS = ItemTags.bind("toughasnails:cooling_items");
        public static final Tag.Named<Item> HEATING_ITEMS = ItemTags.bind("toughasnails:heating_items");

        public static final Tag.Named<Item> ONE_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/1_thirst_drinks");
        public static final Tag.Named<Item> TWO_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/2_thirst_drinks");
        public static final Tag.Named<Item> THREE_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/3_thirst_drinks");
        public static final Tag.Named<Item> FOUR_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/4_thirst_drinks");
        public static final Tag.Named<Item> FIVE_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/5_thirst_drinks");
        public static final Tag.Named<Item> SIX_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/6_thirst_drinks");
        public static final Tag.Named<Item> SEVEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/7_thirst_drinks");
        public static final Tag.Named<Item> EIGHT_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/8_thirst_drinks");
        public static final Tag.Named<Item> NINE_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/9_thirst_drinks");
        public static final Tag.Named<Item> TEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/10_thirst_drinks");
        public static final Tag.Named<Item> ELEVEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/11_thirst_drinks");
        public static final Tag.Named<Item> TWELVE_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/12_thirst_drinks");
        public static final Tag.Named<Item> THIRTEEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/13_thirst_drinks");
        public static final Tag.Named<Item> FOURTEEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/14_thirst_drinks");
        public static final Tag.Named<Item> FIFTEEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/15_thirst_drinks");
        public static final Tag.Named<Item> SIXTEEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/16_thirst_drinks");
        public static final Tag.Named<Item> SEVENTEEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/17_thirst_drinks");
        public static final Tag.Named<Item> EIGHTEEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/18_thirst_drinks");
        public static final Tag.Named<Item> NINETEEN_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/19_thirst_drinks");
        public static final Tag.Named<Item> TWENTY_THIRST_DRINKS = ItemTags.bind("toughasnails:thirst/20_thirst_drinks");

        public static final Tag.Named<Item> TEN_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/10_hydration_drinks");
        public static final Tag.Named<Item> TWENTY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/20_hydration_drinks");
        public static final Tag.Named<Item> THIRTY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/30_hydration_drinks");
        public static final Tag.Named<Item> FOURTY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/40_hydration_drinks");
        public static final Tag.Named<Item> FIFTY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/50_hydration_drinks");
        public static final Tag.Named<Item> SIXTY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/60_hydration_drinks");
        public static final Tag.Named<Item> SEVENTY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/70_hydration_drinks");
        public static final Tag.Named<Item> EIGHTY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/80_hydration_drinks");
        public static final Tag.Named<Item> NINETY_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/90_hydration_drinks");
        public static final Tag.Named<Item> ONE_HUNDRED_HYDRATION_DRINKS = ItemTags.bind("toughasnails:hydration/100_hydration_drinks");

        public static final Tag.Named<Item> TWENTY_FIVE_POISON_CHANCE_DRINKS = ItemTags.bind("toughasnails:poison_chance/25_poison_chance_drinks");
        public static final Tag.Named<Item> FIFTY_POISON_CHANCE_DRINKS = ItemTags.bind("toughasnails:poison_chance/50_poison_chance_drinks");
        public static final Tag.Named<Item> SEVENTY_FIVE_POISON_CHANCE_DRINKS = ItemTags.bind("toughasnails:poison_chance/75_poison_chance_drinks");
        public static final Tag.Named<Item> ONE_HUNDRED_POISON_CHANCE_DRINKS = ItemTags.bind("toughasnails:poison_chance/100_poison_chance_drinks");

        public static final Tag.Named<Item> DRINKS = ItemTags.bind("toughasnails:drinks");
    }
}

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
    }
}

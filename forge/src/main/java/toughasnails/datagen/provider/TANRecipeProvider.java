package toughasnails.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.datagen.recipes.WaterPurifierRecipeBuilder;

public class TANRecipeProvider extends RecipeProvider
{
    public TANRecipeProvider(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        // Canteen
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TANItems.EMPTY_CANTEEN.get()).define('#', Items.LEATHER).pattern(" # ").pattern("# #").pattern(" # ").unlockedBy("has_leather", has(Items.LEATHER)).save(output);

        // Juice
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TANItems.APPLE_JUICE.get()).requires(TANItems.PURIFIED_WATER_BOTTLE.get()).requires(Items.SUGAR).requires(Items.APPLE).group("juice").unlockedBy("has_apple", has(Items.APPLE)).save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TANItems.CACTUS_JUICE.get()).requires(TANItems.PURIFIED_WATER_BOTTLE.get()).requires(Items.SUGAR).requires(Items.CACTUS).group("juice").unlockedBy("has_cactus", has(Items.CACTUS)).save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TANItems.CHORUS_FRUIT_JUICE.get()).requires(TANItems.PURIFIED_WATER_BOTTLE.get()).requires(Items.SUGAR).requires(Items.CHORUS_FRUIT).group("juice").unlockedBy("has_chorus_fruit", has(Items.CHORUS_FRUIT)).save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TANItems.GLOW_BERRY_JUICE.get()).requires(TANItems.PURIFIED_WATER_BOTTLE.get()).requires(Items.SUGAR).requires(Items.GLOW_BERRIES).group("juice").unlockedBy("has_glow_berries", has(Items.GLOW_BERRIES)).save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TANItems.MELON_JUICE.get()).requires(TANItems.PURIFIED_WATER_BOTTLE.get()).requires(Items.SUGAR).requires(Items.MELON_SLICE).group("juice").unlockedBy("has_melon_slice", has(Items.MELON_SLICE)).save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TANItems.PUMPKIN_JUICE.get()).requires(TANItems.PURIFIED_WATER_BOTTLE.get()).requires(Items.SUGAR).requires(Items.PUMPKIN).group("juice").unlockedBy("has_pumpkin", has(Items.PUMPKIN)).save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, TANItems.SWEET_BERRY_JUICE.get()).requires(TANItems.PURIFIED_WATER_BOTTLE.get()).requires(Items.SUGAR).requires(Items.SWEET_BERRIES).group("juice").unlockedBy("has_sweet_berries", has(Items.SWEET_BERRIES)).save(output);

        // Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TANBlocks.RAIN_COLLECTOR.get()).define('S', Items.STRING).define('I', Items.IRON_INGOT).define('B', Items.BARREL).pattern("ISI").pattern("IBI").unlockedBy("has_barrel", has(Items.BARREL)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TANBlocks.WATER_PURIFIER.get()).define('W', ItemTags.PLANKS).define('S', ItemTags.SAND).define('C', Items.CHARCOAL).define('G', Items.GLASS).pattern("WSW").pattern("GCG").pattern("GGG").unlockedBy("has_charcoal", has(Items.CHARCOAL)).save(output);

        // Armor
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.LEAF_BOOTS.get()).define('X', ItemTags.LEAVES).pattern("X X").pattern("X X").unlockedBy("has_leaves", has(ItemTags.LEAVES)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.LEAF_CHESTPLATE.get()).define('X', ItemTags.LEAVES).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_leaves", has(ItemTags.LEAVES)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.LEAF_HELMET.get()).define('X', ItemTags.LEAVES).pattern("XXX").pattern("X X").unlockedBy("has_leaves", has(ItemTags.LEAVES)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.LEAF_LEGGINGS.get()).define('X', ItemTags.LEAVES).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_leaves", has(ItemTags.LEAVES)).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.WOOL_BOOTS.get()).define('X', ItemTags.WOOL).pattern("X X").pattern("X X").unlockedBy("has_wool", has(ItemTags.WOOL)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.WOOL_CHESTPLATE.get()).define('X', ItemTags.WOOL).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_wool", has(ItemTags.WOOL)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.WOOL_HELMET.get()).define('X', ItemTags.WOOL).pattern("XXX").pattern("X X").unlockedBy("has_wool", has(ItemTags.WOOL)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TANItems.WOOL_LEGGINGS.get()).define('X', ItemTags.WOOL).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_wool", has(ItemTags.WOOL)).save(output);

        //
        // Water purification recipes
        //

        // Water bottle
        waterPurifier(output, new ItemStack(TANItems.DIRTY_WATER_BOTTLE.get()), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), 400);
        waterPurifier(output, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), new ItemStack(TANItems.PURIFIED_WATER_BOTTLE.get()), 200);

        // Canteens
        waterPurifier(output, new ItemStack(TANItems.DIRTY_WATER_CANTEEN.get()), new ItemStack(TANItems.WATER_CANTEEN.get()), 400);
        waterPurifier(output, new ItemStack(TANItems.WATER_CANTEEN.get()), new ItemStack(TANItems.PURIFIED_WATER_CANTEEN.get()), 200);
    }

    public static void waterPurifier(RecipeOutput output, ItemStack input, ItemStack result, int purifyTime)
    {
        WaterPurifierRecipeBuilder.waterPurifier(input, result, purifyTime).save(output, new ResourceLocation(ToughAsNails.MOD_ID, getItemName(result.getItem())));
    }
}

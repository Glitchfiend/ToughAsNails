package toughasnails.init;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import toughasnails.api.TANBlocks;
import toughasnails.api.TANPotions;
import toughasnails.api.item.TANItems;
import toughasnails.block.BlockTANTemperatureCoil;
import toughasnails.item.ItemFruitJuice;
import toughasnails.item.ItemTANWaterBottle;

public class ModCrafting
{
    public static void init()
    {
        addOreRegistration();
        addCraftingRecipies();
        addSmeltingRecipes();
        //removeCraftingRecipes();
    }
    
    private static void addCraftingRecipies()
    {
    	// Register crafting recipes
    	
    	//Brewing
    	//Base
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), new ItemStack(Items.FIRE_CHARGE), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), new ItemStack(TANItems.ice_charge), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.cold_resistance_type));
    	
    	//Extended
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.heat_resistance_type), new ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.long_heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.cold_resistance_type), new ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.long_cold_resistance_type));
    	
    	//Splash
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.heat_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.cold_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.cold_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.long_heat_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.long_cold_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_cold_resistance_type));
    	
    	//Extended Splash
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.heat_resistance_type), new ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.cold_resistance_type), new ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_cold_resistance_type));
    	
    	//Lingering -> Splash
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.heat_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.cold_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.cold_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_heat_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_cold_resistance_type), new ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_cold_resistance_type));
    	
    	//Lingering
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.heat_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.cold_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.cold_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.long_heat_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TANPotions.long_cold_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_cold_resistance_type));
    	
    	//Lingering Extended
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.heat_resistance_type), new ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.cold_resistance_type), new ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_cold_resistance_type));
    	
    	//Splash -> Lingering
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.heat_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.cold_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.cold_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_heat_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_heat_resistance_type));
    	BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), TANPotions.long_cold_resistance_type), new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), TANPotions.long_cold_resistance_type));
    	
    	// Armor
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_helmet), new Object [] {"###", "# #", '#', Blocks.WOOL});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_chestplate), new Object [] {"# #", "###", "###", '#', Blocks.WOOL});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_leggings), new Object [] {"###", "# #", "# #", '#', Blocks.WOOL});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_boots), new Object [] {"# #", "# #", '#', Blocks.WOOL});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_helmet), new Object [] {"###", "# #", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_chestplate), new Object [] {"# #", "###", "###", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_leggings), new Object [] {"###", "# #", "# #", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_boots), new Object [] {"# #", "# #", '#', TANItems.jelled_slime});
        
        // Campfire
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANBlocks.campfire), new Object [] {" L ", "LLL", "CCC", 'C', Blocks.COBBLESTONE, 'L', "logWood"}));
        
        // Heating Coil
    	GameRegistry.addShapedRecipe(new ItemStack(TANBlocks.temperature_coil, 1, BlockTANTemperatureCoil.CoilType.HEATING.ordinal()), new Object[] {"BBB", "BBB", "CCC", 'B', Items.BLAZE_ROD, 'C', Blocks.COBBLESTONE});

    	// Cooling Coil
    	GameRegistry.addShapedRecipe(new ItemStack(TANBlocks.temperature_coil, 1, BlockTANTemperatureCoil.CoilType.COOLING.ordinal()), new Object[] {"FFF", "FFF", "CCC", 'F', TANItems.freeze_rod, 'C', Blocks.COBBLESTONE});
        
    	// Canteen
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.canteen, 1), new Object[] {" L ", "L L", "LLL", 'L', Items.LEATHER});
    	
    	// Filtered Water Bottle
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.water_bottle, 1, ItemTANWaterBottle.WaterBottleType.FILTERED.ordinal()), new Object[] {new ItemStack(TANItems.water_bottle, 1, ItemTANWaterBottle.WaterBottleType.DIRTY.ordinal()), TANItems.charcoal_filter});

    	// Fruit Juices
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.APPLE.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.APPLE});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.BEETROOT.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.BEETROOT});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.CACTUS.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Blocks.CACTUS});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.CARROT.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.CARROT});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.CHORUS_FRUIT.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.CHORUS_FRUIT});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.GLISTERING_MELON.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.SPECKLED_MELON});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.GOLDEN_APPLE.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.GOLDEN_APPLE});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.GOLDEN_CARROT.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.GOLDEN_CARROT});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.MELON.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Items.MELON});
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.fruit_juice, 1, ItemFruitJuice.JuiceType.PUMPKIN.ordinal()), new Object[] {PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), Items.SUGAR, Blocks.PUMPKIN});
    	
    	// Freeze Powder
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.freeze_powder, 2), new Object[] {TANItems.freeze_rod});
    	
    	// Ice Charge
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.ice_charge, 3), new Object[] {TANItems.ice_cube, Items.GUNPOWDER, TANItems.freeze_powder});
    	
    	// Jelled Slime
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime, 3), new Object[] {"III", "ISI", "III", 'I', TANItems.ice_cube, 'S', Items.SLIME_BALL});
    	
    	//Charcoal Filter
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.charcoal_filter, 3), new Object[] {"PPP", "CCC", "PPP", 'P', Items.PAPER, 'C', new ItemStack(Items.COAL, 1, 1)});
    	
    	// Air Filter
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.air_filter, 1), new Object[] {"LLL", "LCL", "LLL", 'L', Items.LEATHER, 'C', TANItems.charcoal_filter});
    	
    	// Season Clock
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.season_clock, 1), new Object[] {" Q ", "QRQ", " Q ", 'Q', Items.QUARTZ, 'R', Items.REDSTONE});
    	
    	// Season Sensor
    	GameRegistry.addShapedRecipe(new ItemStack(TANBlocks.season_sensors[0], 1), new Object[] {"GGG", "QSQ", "CCC", 'G', Blocks.GLASS, 'Q', Items.QUARTZ, 'S', TANItems.season_clock, 'C', new ItemStack(Blocks.STONE_SLAB, 1, 3)});
    	
    	// Thermometer
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.thermometer, 1), new Object[] {" D ", "DQD", " D ", 'D', Items.DIAMOND, 'Q', Items.QUARTZ});
    	
    	// Respirator
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.respirator, 1), new Object[] {"SSS", "III", "AIA", 'S', Items.STRING, 'I', Items.IRON_INGOT, 'A', TANItems.air_filter});
    
    	// Vanilla Materials
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.ordinal()), new Object[] {"SS", "SS", 'S', TANItems.stone_chunk});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.ordinal()), new Object[] {"SS", "SS", 'S', TANItems.stone_chunk_andesite});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.ordinal()), new Object[] {"SS", "SS", 'S', TANItems.stone_chunk_diorite});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.ordinal()), new Object[] {"SS", "SS", 'S', TANItems.stone_chunk_granite});
    	
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.COBBLESTONE, 1), new Object[] {"SC", "CS", 'S', TANItems.stone_chunk, 'C', Items.CLAY_BALL});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.COBBLESTONE, 1), new Object[] {"CS", "SC", 'S', TANItems.stone_chunk, 'C', Items.CLAY_BALL});
    	
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.DIRT, 1), new Object[] {"PP", "PP", 'P', TANItems.pile_of_dirt});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.GRAVEL, 1), new Object[] {"PP", "PP", 'P', TANItems.pile_of_gravel});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.SAND, 1), new Object[] {"PP", "PP", 'P', TANItems.pile_of_sand});
    	
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.NETHERRACK, 1), new Object[] {"CC", "CC", 'C', TANItems.netherrack_chunk});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.END_STONE, 1), new Object[] {"CC", "CC", 'C', TANItems.end_stone_chunk});
    	
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.OBSIDIAN, 1), new Object[] {"SS", "SS", 'S', TANItems.obsidian_shard});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.GLASS, 1), new Object[] {"SS", "SS", 'S', TANItems.glass_shard});
    	
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.ordinal()), new Object[] {"BB", "BB", 'B', TANItems.bark_oak});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.SPRUCE.ordinal()), new Object[] {"BB", "BB", 'B', TANItems.bark_spruce});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.BIRCH.ordinal()), new Object[] {"BB", "BB", 'B', TANItems.bark_birch});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.JUNGLE.ordinal()), new Object[] {"BB", "BB", 'B', TANItems.bark_jungle});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.ACACIA.ordinal()), new Object[] {"BB", "BB", 'B', TANItems.bark_acacia});
    	GameRegistry.addShapedRecipe(new ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.DARK_OAK.ordinal()), new Object[] {"BB", "BB", 'B', TANItems.bark_dark_oak});
    	
    	GameRegistry.addShapedRecipe(new ItemStack(Items.IRON_INGOT, 1), new Object[] {"III", "III", "III", 'I', TANItems.iron_nugget});
    }
    
    public static void addSmeltingRecipes()
    {
    	// Register smelting recipes
    	
    	// Clean Water Bottle
    	GameRegistry.addSmelting(new ItemStack(TANItems.water_bottle, 1, ItemTANWaterBottle.WaterBottleType.FILTERED.ordinal()), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), 0F);
    	
    	// Vanilla Materials
    	GameRegistry.addSmelting(new ItemStack(TANItems.pile_of_sand), new ItemStack(TANItems.glass_shard), 0.05F);
    	GameRegistry.addSmelting(new ItemStack(TANItems.gold_ore_chunk), new ItemStack(Items.GOLD_NUGGET), 0.5F);
    	GameRegistry.addSmelting(new ItemStack(TANItems.iron_ore_chunk), new ItemStack(TANItems.iron_nugget), 0.3F);
    }
    
    private static void addOreRegistration()
    {
    	//Registration in Ore Dictionary
    }
    
    /*private static void removeCraftingRecipes()
    {
    	List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
    	
    	Iterator<IRecipe> remover = recipes.iterator();
    	
    	while (remover.hasNext())
    	{
    		ItemStack itemstack = remover.next().getRecipeOutput();
    		if (itemstack != null && Block.getBlockFromItem(itemstack.getItem()) == Blocks.torch)
    		{
    			remover.remove();
    		}
    	}
    }*/
}

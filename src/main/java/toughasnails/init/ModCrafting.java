package toughasnails.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
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
import toughasnails.util.PotionBrewingRecipe;

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
    	addBrewingRecipe(PotionTypes.AWKWARD, new ItemStack(Items.FIRE_CHARGE), TANPotions.heat_resistance_type);
    	addBrewingRecipe(PotionTypes.AWKWARD, new ItemStack(TANItems.ice_charge), TANPotions.cold_resistance_type);
   	
    	//Extended
    	addBrewingRecipe(TANPotions.heat_resistance_type, new ItemStack(Items.REDSTONE), TANPotions.long_heat_resistance_type);
    	addBrewingRecipe(TANPotions.cold_resistance_type, new ItemStack(Items.REDSTONE), TANPotions.long_cold_resistance_type);
   	
    	//Splash and lingering
    	addPotionTransforms(TANPotions.heat_resistance_type);
    	addPotionTransforms(TANPotions.cold_resistance_type);
    	addPotionTransforms(TANPotions.long_heat_resistance_type);
    	addPotionTransforms(TANPotions.long_cold_resistance_type);

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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANBlocks.campfire), new Object [] {" L ", "LLL", "CCC", 'C', "chunkStone", 'L', "splitWood"}));
        
        // Rain Collector
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANBlocks.rain_collector), new Object [] {"IBI", "C C", "CCC", 'C', Blocks.COBBLESTONE, 'I', "ingotIron", 'B', Blocks.IRON_BARS}));
        
        // Heating Coil
    	GameRegistry.addShapedRecipe(new ItemStack(TANBlocks.temperature_coil, 1, BlockTANTemperatureCoil.CoilType.HEATING.ordinal()), new Object[] {"BBB", "BBB", "CCC", 'B', Items.BLAZE_ROD, 'C', Blocks.COBBLESTONE});

    	// Cooling Coil
    	GameRegistry.addShapedRecipe(new ItemStack(TANBlocks.temperature_coil, 1, BlockTANTemperatureCoil.CoilType.COOLING.ordinal()), new Object[] {"FFF", "FFF", "CCC", 'F', TANItems.freeze_rod, 'C', Blocks.COBBLESTONE});
        
    	// Canteen
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.canteen, 1), new Object[] {" L ", "L L", "LLL", 'L', Items.LEATHER});
    	
    	// Filtered Canteen
        GameRegistry.addShapelessRecipe(new ItemStack(TANItems.canteen, 1, 2), TANItems.charcoal_filter, new ItemStack(TANItems.canteen, 1, 1));
    	
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
    	//GameRegistry.addShapedRecipe(new ItemStack(TANItems.air_filter, 1), new Object[] {"LLL", "LCL", "LLL", 'L', Items.LEATHER, 'C', TANItems.charcoal_filter});
    	
    	// Season Clock
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.season_clock, 1), new Object[] {" Q ", "QRQ", " Q ", 'Q', Items.QUARTZ, 'R', Items.REDSTONE});
    	
    	// Season Sensor
    	GameRegistry.addShapedRecipe(new ItemStack(TANBlocks.season_sensors[0], 1), new Object[] {"GGG", "QSQ", "CCC", 'G', Blocks.GLASS, 'Q', Items.QUARTZ, 'S', TANItems.season_clock, 'C', new ItemStack(Blocks.STONE_SLAB, 1, 3)});
    	
    	// Thermometer
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.thermometer, 1), new Object[] {" D ", "DQD", " D ", 'D', Items.DIAMOND, 'Q', Items.QUARTZ});
    	
    	// Respirator
    	//GameRegistry.addShapedRecipe(new ItemStack(TANItems.respirator, 1), new Object[] {"SSS", "III", "AIA", 'S', Items.STRING, 'I', Items.IRON_INGOT, 'A', TANItems.air_filter});
    }
    
    public static void addSmeltingRecipes()
    {
    	// Register smelting recipes
    	
    	// Clean Water Bottle
    	GameRegistry.addSmelting(new ItemStack(TANItems.water_bottle, 1, ItemTANWaterBottle.WaterBottleType.FILTERED.ordinal()), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), 0F);
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
    
    /**
     * Adds a brewing recipe for each bottle to the specified potion type transformation
     * @param input       Input potion
     * @param ingredient  Transformation ingredient
     * @param output      Output potion
     */
    private static void addBrewingRecipe(PotionType input, ItemStack ingredient, PotionType output)
    {
    	addBrewingRecipe(new ItemStack(Items.POTIONITEM), input, ingredient, new ItemStack(Items.POTIONITEM), output);
    	addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), input, ingredient, new ItemStack(Items.SPLASH_POTION), output);
    	addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), input, ingredient, new ItemStack(Items.LINGERING_POTION), output);
    }
    
    /**
     * Adds recipes to transform potions into splash and lingering variants
     * @param potion  Potion type to add transformations
     */
    private static void addPotionTransforms(PotionType potion)
    {
    	// splash
    	addBrewingRecipe(new ItemStack(Items.POTIONITEM), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
    	addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
    	
    	// lingering
    	addBrewingRecipe(new ItemStack(Items.POTIONITEM), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
    	addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);

    }
    
    /**
     * Adds a general brewing recipe. Basically just a wrapper to remove the ugly PotionUtils calls and to use the NBT sensitive PotionBrewingRecipe
     * @param inBottle    Input bottle
     * @param input       Input potion
     * @param ingredient  Input ingredient
     * @param outBottle   Output bottle
     * @param output      Output potion
     */
    private static void addBrewingRecipe(ItemStack inBottle, PotionType input, ItemStack ingredient, ItemStack outBottle, PotionType output)
    {
    	BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(PotionUtils.addPotionToItemStack(inBottle, input), ingredient, PotionUtils.addPotionToItemStack(outBottle, output)));
    }
}

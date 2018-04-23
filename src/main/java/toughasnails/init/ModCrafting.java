package toughasnails.init;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.api.TANPotions;
import toughasnails.api.item.TANItems;
import toughasnails.util.PotionBrewingRecipe;

public class ModCrafting
{
    public static void init()
    {
        addOreRegistration();
        addCraftingRecipies();
        addSmeltingRecipes();
    }
    
    private static void addCraftingRecipies() {
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
	}

    public static void addSmeltingRecipes()
    {
    	// Register smelting recipes
    	
    	// Purified Water Bottle
    	GameRegistry.addSmelting(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new ItemStack(TANItems.purified_water_bottle, 1), 0F);
    }
    
    private static void addOreRegistration()
    {
    	//Registration in Ore Dictionary
    }
    
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

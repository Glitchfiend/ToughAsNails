package tan.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import cpw.mods.fml.common.registry.GameRegistry;

public class TANCrafting
{
    public static void init()
    {
        addShapedRecipes();
        addShapelessRecipes();
        addSmeltingRecipes();
    }
    
    private static void addShapedRecipes()
    {
        //Canteen
    	GameRegistry.addRecipe(new ItemStack(TANItems.canteen, 1, 0), new Object[] {" L ", "L L", "LLL", 'L', new ItemStack(Item.leather, 1, 0)});
    	
    	//Thermometer
    	GameRegistry.addRecipe(new ItemStack(TANItems.thermometer, 1, 0), new Object[] {" D ", "DQD", " D ", 'D', new ItemStack(Item.diamond, 1, 0), 'Q', new ItemStack(Item.netherQuartz, 1, 0)});
    
    	//Charcoal Filter
    	GameRegistry.addRecipe(new ItemStack(TANItems.miscItems, 1, 0), new Object[] {"PPP", "CCC", "PPP", 'P', new ItemStack(Item.paper, 1, 0), 'C', new ItemStack(Item.coal, 1, 1)});
    	
    	//Wool Armor
    	//Hood
    	GameRegistry.addRecipe(new ItemStack(TANArmour.helmetWool, 1, 0), new Object[] {"CCC", "C C", 'C', Block.cloth});
    	//Coat
    	GameRegistry.addRecipe(new ItemStack(TANArmour.chestplateWool, 1, 0), new Object[] {"C C", "CCC", "CCC", 'C', Block.cloth});
    	//Pants
    	GameRegistry.addRecipe(new ItemStack(TANArmour.leggingsWool, 1, 0), new Object[] {"CCC", "C C", "C C", 'C', Block.cloth});
    	//Boots
    	GameRegistry.addRecipe(new ItemStack(TANArmour.bootsWool, 1, 0), new Object[] {"C C", "C C", 'C', Block.cloth});
    	
    	//Heat Resistant Armor
    	//Helmet
    	GameRegistry.addRecipe(new ItemStack(TANArmour.helmetHeat, 1, 0), new Object[] {"SSS", "S S", 'S', new ItemStack(TANItems.miscItems, 1, 1)});
    	//Chestplate
    	GameRegistry.addRecipe(new ItemStack(TANArmour.chestplateHeat, 1, 0), new Object[] {"S S", "SSS", "SSS", 'S', new ItemStack(TANItems.miscItems, 1, 1)});
    	//Leggings
    	GameRegistry.addRecipe(new ItemStack(TANArmour.leggingsHeat, 1, 0), new Object[] {"SSS", "S S", "S S", 'S', new ItemStack(TANItems.miscItems, 1, 1)});
    	//Boots
    	GameRegistry.addRecipe(new ItemStack(TANArmour.bootsHeat, 1, 0), new Object[] {"S S", "S S", 'S', new ItemStack(TANItems.miscItems, 1, 1)});
    }
    
    private static void addShapelessRecipes()
    {
        
    }
    
	private static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(Item.potion.itemID, 0, new ItemStack(TANItems.waterBottle, 1, 0), 0F);
		GameRegistry.addSmelting(Item.slimeBall.itemID, new ItemStack(TANItems.miscItems, 1, 1), 0.1F);
	}
}

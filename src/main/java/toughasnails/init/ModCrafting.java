package toughasnails.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.api.TANItems;

public class ModCrafting
{
    public static void init()
    {
        addOreRegistration();
        addCraftingRecipies();
        addSmeltingRecipes();
    }
    
    private static void addCraftingRecipies()
    {
    	// Register crafting recipes
    	
    	//Armor
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_helmet), new Object [] {"###", "# #", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_chestplate), new Object [] {"# #", "###", "###", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_leggings), new Object [] {"###", "# #", "# #", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_boots), new Object [] {"# #", "# #", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_helmet), new Object [] {"###", "# #", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_chestplate), new Object [] {"# #", "###", "###", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_leggings), new Object [] {"###", "# #", "# #", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_boots), new Object [] {"# #", "# #", '#', TANItems.jelled_slime});
        
        
    	// Canteen
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.canteen, 1), new Object[] {"SL ", "L L", "LLL", 'S', Items.string, 'L', Items.leather});
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.canteen, 1), new Object[] {" LS", "L L", "LLL", 'S', Items.string, 'L', Items.leather});
    	
    	//Charcoal Filter
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.charcoal_filter, 1), new Object[] {"PPP", "CCC", "PPP", 'P', Items.paper, 'C', new ItemStack(Items.coal, 1, 1)});
    }
    
    public static void addSmeltingRecipes()
    {
    	// Register smelting recipes
    	
    	// Jelled Slime
    	GameRegistry.addSmelting(new ItemStack(Items.slime_ball), new ItemStack(TANItems.jelled_slime), 0F);
    }
    
    private static void addOreRegistration()
    {
    	//Registration in Ore Dictionary
    }
}

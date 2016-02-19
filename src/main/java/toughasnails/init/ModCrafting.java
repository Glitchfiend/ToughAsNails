package toughasnails.init;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import toughasnails.api.TANBlocks;
import toughasnails.api.TANItems;
import toughasnails.item.ItemArrowTopper;
import toughasnails.item.ItemTANArrow;

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
    	
    	// Armor
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_helmet), new Object [] {"###", "# #", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_chestplate), new Object [] {"# #", "###", "###", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_leggings), new Object [] {"###", "# #", "# #", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.wool_boots), new Object [] {"# #", "# #", '#', Blocks.wool});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_helmet), new Object [] {"###", "# #", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_chestplate), new Object [] {"# #", "###", "###", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_leggings), new Object [] {"###", "# #", "# #", '#', TANItems.jelled_slime});
        GameRegistry.addShapedRecipe(new ItemStack(TANItems.jelled_slime_boots), new Object [] {"# #", "# #", '#', TANItems.jelled_slime});
        
        // Campfire
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANBlocks.campfire), new Object [] {" L ", "LLL", "CCC", 'C', Blocks.cobblestone, 'L', "logWood"}));
        
    	// Canteen
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.canteen, 1), new Object[] {" L ", "L L", "LLL", 'L', Items.leather});
    	
    	// Arrows
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANItems.arrow, 4, ItemTANArrow.ArrowType.FIRE_ARROW.ordinal()), new Object [] {"T", "S", "F", 'T', new ItemStack(TANItems.arrow_topper, 1, ItemArrowTopper.TopperType.FIRE_ARROW_TOPPER.ordinal()), 'S', "stickWood", 'F', Items.feather}));
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANItems.arrow, 4, ItemTANArrow.ArrowType.ICE_ARROW.ordinal()), new Object [] {"T", "S", "F", 'T', new ItemStack(TANItems.arrow_topper, 1, ItemArrowTopper.TopperType.ICE_ARROW_TOPPER.ordinal()), 'S', "stickWood", 'F', Items.feather}));
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANItems.arrow, 4, ItemTANArrow.ArrowType.LIGHTNING_ARROW.ordinal()), new Object [] {"T", "S", "F", 'T', new ItemStack(TANItems.arrow_topper, 1, ItemArrowTopper.TopperType.LIGHTNING_ARROW_TOPPER.ordinal()), 'S', "stickWood", 'F', Items.feather}));
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TANItems.arrow, 4, ItemTANArrow.ArrowType.BOMB_ARROW.ordinal()), new Object [] {"T", "S", "F", 'T', new ItemStack(TANItems.arrow_topper, 1, ItemArrowTopper.TopperType.BOMB_ARROW_TOPPER.ordinal()), 'S', "stickWood", 'F', Items.feather}));
    	
    	// Arrow Toppers
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.arrow_topper, 2, ItemArrowTopper.TopperType.FIRE_ARROW_TOPPER.ordinal()), new Object [] {" B ", "BCB", " B ", 'B', Items.blaze_powder, 'C', Items.coal});
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.arrow_topper, 2, ItemArrowTopper.TopperType.ICE_ARROW_TOPPER.ordinal()), new Object [] {" F ", "FIF", " F ", 'F', TANItems.freeze_powder, 'I', TANItems.ice_cube});
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.arrow_topper, 2, ItemArrowTopper.TopperType.LIGHTNING_ARROW_TOPPER.ordinal()), new Object [] {" P ", "PNP", " P ", 'P', Items.prismarine_crystals, 'N', Items.nether_star});
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.arrow_topper, 2, ItemArrowTopper.TopperType.BOMB_ARROW_TOPPER.ordinal()), new Object [] {" C ", "CGC", " C ", 'C', Items.clay_ball, 'G', Items.gunpowder});
    	
    	// Freeze Powder
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.freeze_powder, 2), new Object[] {TANItems.freeze_rod});
    	
    	// Ice Charge
    	GameRegistry.addShapelessRecipe(new ItemStack(TANItems.ice_charge, 3), new Object[] {TANItems.ice_cube, Items.gunpowder, TANItems.freeze_powder});
    	
    	//Charcoal Filter
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.charcoal_filter, 1), new Object[] {"PPP", "CCC", "PPP", 'P', Items.paper, 'C', new ItemStack(Items.coal, 1, 1)});
    	
    	// Air Filter
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.air_filter, 1), new Object[] {"LLL", "LCL", "LLL", 'L', Items.leather, 'C', TANItems.charcoal_filter});
    	
    	// Backpack
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.backpack, 1), new Object[] {"LLL", "SLS", "LLL", 'L', Items.leather, 'S', Items.string});
    	
    	// Respirator
    	GameRegistry.addShapedRecipe(new ItemStack(TANItems.respirator, 1), new Object[] {"SSS", "III", "AIA", 'S', Items.string, 'I', Items.iron_ingot, 'A', TANItems.air_filter});
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

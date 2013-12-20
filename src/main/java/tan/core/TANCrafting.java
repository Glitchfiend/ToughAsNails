package tan.core;

import static com.google.common.base.Preconditions.checkNotNull;
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
    }
    
    private static void addShapelessRecipes()
    {
        
    }
    
	private static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(Item.potion.itemID, 0, new ItemStack(TANItems.freshWaterBottle, 1, 0), 0F);
	}
}

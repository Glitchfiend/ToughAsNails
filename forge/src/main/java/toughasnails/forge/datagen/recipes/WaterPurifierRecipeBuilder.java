package toughasnails.forge.datagen.recipes;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import toughasnails.crafting.WaterPurifierRecipe;

public class WaterPurifierRecipeBuilder
{
    private final ItemStack input;
    private final ItemStack result;
    private final int purifyTime;

    private WaterPurifierRecipeBuilder(ItemStack input, ItemStack result, int purifyTime)
    {
        this.input = input;
        this.result = result;
        this.purifyTime = purifyTime;
    }

    public static WaterPurifierRecipeBuilder waterPurifier(ItemStack input, ItemStack result, int purifyTime)
    {
        return new WaterPurifierRecipeBuilder(input, result, purifyTime);
    }

    public void save(RecipeOutput output, String name)
    {
        this.save(output, new ResourceLocation(name));
    }

    public void save(RecipeOutput output, ResourceLocation location)
    {
        output.accept(location, new WaterPurifierRecipe(this.input, this.result, this.purifyTime), null);
    }
}

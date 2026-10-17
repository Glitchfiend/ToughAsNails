package toughasnails.neoforge.datagen.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import toughasnails.crafting.WaterPurifierRecipe;

public class WaterPurifierRecipeBuilder
{
    private final ItemStackTemplate input;
    private final ItemStackTemplate result;
    private final int purifyTime;

    private WaterPurifierRecipeBuilder(ItemStackTemplate input, ItemStackTemplate result, int purifyTime)
    {
        this.input = input;
        this.result = result;
        this.purifyTime = purifyTime;
    }

    public static WaterPurifierRecipeBuilder waterPurifier(ItemStackTemplate input, ItemStackTemplate result, int purifyTime)
    {
        return new WaterPurifierRecipeBuilder(input, result, purifyTime);
    }

    public void save(RecipeOutput output, String name)
    {
        this.save(output, Identifier.parse(name));
    }

    public void save(RecipeOutput output, Identifier location)
    {
        output.accept(ResourceKey.create(Registries.RECIPE, location), new WaterPurifierRecipe(this.input, this.result, this.purifyTime), null);
    }
}

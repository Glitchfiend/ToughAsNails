/*******************************************************************************
 * Copyright 2026, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.provider;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.BrewingRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Recipe;
import toughasnails.api.potion.TANPotions;
import toughasnails.core.ToughAsNails;

import java.util.List;

public class TANBrewingProvider
{
    private static final List<Item> CONTAINERS = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);

    private final RecipeOutput output;

    public TANBrewingProvider(RecipeOutput output)
    {
        this.output = output;
    }

    public void buildRecipes()
    {
        this.brewingMix(Potions.AWKWARD, Items.SNOWBALL, TANPotions.ICE_RESISTANCE);
        this.brewingMix(TANPotions.ICE_RESISTANCE, Items.REDSTONE, TANPotions.LONG_ICE_RESISTANCE);

        for (Holder<Potion> potion : List.of(TANPotions.ICE_RESISTANCE, TANPotions.LONG_ICE_RESISTANCE))
        {
            this.brewingContainerTransform(Items.POTION, potion, Items.GUNPOWDER, Items.SPLASH_POTION);
            this.brewingContainerTransform(Items.SPLASH_POTION, potion, Items.DRAGON_BREATH, Items.LINGERING_POTION);
        }
    }

    private void brewingMix(Holder<Potion> input, Item reagent, Holder<Potion> output)
    {
        for (Item container : CONTAINERS)
        {
            BrewingRecipeBuilder.brewingMix(container, input, reagent, output).save(this.output, recipeKey(container, input, reagent));
        }
    }

    private void brewingContainerTransform(Item container, Holder<Potion> potion, Item reagent, Item outputContainer)
    {
        BrewingRecipeBuilder.brewingContainerTransform(container, potion, reagent, outputContainer).save(this.output, recipeKey(container, potion, reagent));
    }

    private static ResourceKey<Recipe<?>> recipeKey(Item container, Holder<Potion> potion, Item reagent)
    {
        String path = "brewing/" + BuiltInRegistries.ITEM.getKey(container).getPath() + "_" + potion.unwrapKey().orElseThrow().identifier().getPath() + "_" + BuiltInRegistries.ITEM.getKey(reagent).getPath();
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, path));
    }
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.*;
import toughasnails.api.TANAPI;
import toughasnails.api.crafting.TANRecipeBookCategories;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.crafting.WaterPurifierRecipe;

import java.util.function.BiConsumer;

public class ModCrafting
{
    public static void registerRecipeSerializers(BiConsumer<Identifier, RecipeSerializer<?>> func)
    {
        TANRecipeSerializers.WATER_PURIFYING = (RecipeSerializer<? extends Recipe<SingleRecipeInput>>) registerSerializer(func, "water_purifying", new WaterPurifierRecipe.Serializer());
    }

    public static void registerRecipeTypes(BiConsumer<Identifier, RecipeType<?>> func)
    {
        TANRecipeTypes.WATER_PURIFYING = (RecipeType<? extends Recipe<SingleRecipeInput>>) registerRecipe(func, "water_purifying", new RecipeType<WaterPurifierRecipe>()
        {
            @Override
            public String toString()
            {
                return "water_purifying";
            }
        });
    }

    public static void registerRecipeBookCategories(BiConsumer<Identifier, RecipeBookCategory> func)
    {
        TANRecipeBookCategories.WATER_PURIFYING = registerRecipeBookCategory(func, "water_purifying", new RecipeBookCategory());
    }

    private static RecipeSerializer<?> registerSerializer(BiConsumer<Identifier, RecipeSerializer<?>> func, String name, RecipeSerializer<?> serializer)
    {
        func.accept(Identifier.fromNamespaceAndPath(TANAPI.MOD_ID, name), serializer);
        return serializer;
    }

    private static <T> RecipeType<?> registerRecipe(BiConsumer<Identifier, RecipeType<?>> func, String name, RecipeType<?> type)
    {
        func.accept(Identifier.fromNamespaceAndPath(TANAPI.MOD_ID, name), type);
        return type;
    }

    private static RecipeBookCategory registerRecipeBookCategory(BiConsumer<Identifier, RecipeBookCategory> func, String name, RecipeBookCategory category)
    {
        func.accept(Identifier.fromNamespaceAndPath(TANAPI.MOD_ID, name), category);
        return category;
    }
}

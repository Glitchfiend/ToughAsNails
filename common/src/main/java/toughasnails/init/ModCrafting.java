/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import toughasnails.api.TANAPI;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;

import java.util.function.BiConsumer;
import toughasnails.crafting.WaterPurifierRecipe;

public class ModCrafting
{
    public static void registerRecipeSerializers(BiConsumer<ResourceLocation, RecipeSerializer<?>> func)
    {
        TANRecipeSerializers.WATER_PURIFYING = registerSerializer(func, "water_purifying", new WaterPurifierRecipe.Serializer());
    }

    public static void registerRecipeTypes(BiConsumer<ResourceLocation, RecipeType<?>> func)
    {
        TANRecipeTypes.WATER_PURIFYING = registerRecipe(func, "water_purifying", new RecipeType<WaterPurifierRecipe>()
        {
            @Override
            public String toString()
            {
                return "water_purifying";
            }
        });
    }

    private static RecipeSerializer<?> registerSerializer(BiConsumer<ResourceLocation, RecipeSerializer<?>> func, String name, RecipeSerializer<?> serializer)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), serializer);
        return serializer;
    }

    private static RecipeType<?> registerRecipe(BiConsumer<ResourceLocation, RecipeType<?>> func, String name, RecipeType<?> type)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), type);
        return type;
    }
}

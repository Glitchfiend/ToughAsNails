/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import toughasnails.api.TANAPI;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.api.potion.TANPotions;
import toughasnails.crafting.WaterPurifierRecipe;

import java.util.function.BiConsumer;

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
        func.accept(ResourceLocation.fromNamespaceAndPath(TANAPI.MOD_ID, name), serializer);
        return serializer;
    }

    private static RecipeType<?> registerRecipe(BiConsumer<ResourceLocation, RecipeType<?>> func, String name, RecipeType<?> type)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(TANAPI.MOD_ID, name), type);
        return type;
    }
}

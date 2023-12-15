/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.util.RecipeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
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

    public static void registerPotionRecipes()
    {
        // Brewing
        // Base
        addBrewingRecipe(Potions.AWKWARD, new ItemStack(Items.SNOWBALL), TANPotions.ICE_RESISTANCE);

        // Extended
        addBrewingRecipe(TANPotions.ICE_RESISTANCE, new ItemStack(Items.REDSTONE), TANPotions.LONG_ICE_RESISTANCE);

        // Splash and lingering
        addPotionTransforms(TANPotions.ICE_RESISTANCE);
        addPotionTransforms(TANPotions.LONG_ICE_RESISTANCE);
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

    private static void addBrewingRecipe(Potion input, ItemStack ingredient, Potion output)
    {
        addBrewingRecipe(new ItemStack(Items.POTION), input, ingredient, new ItemStack(Items.POTION), output);
        addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), input, ingredient, new ItemStack(Items.SPLASH_POTION), output);
        addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), input, ingredient, new ItemStack(Items.LINGERING_POTION), output);
    }

    private static void addPotionTransforms(Potion potion)
    {
        // Splash
        addBrewingRecipe(new ItemStack(Items.POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
        addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);

        // Lingering
        addBrewingRecipe(new ItemStack(Items.POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
        addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
    }

    private static void addBrewingRecipe(ItemStack bottleIn, Potion potionIn, ItemStack ingredient, ItemStack bottleOut, Potion potionOut)
    {
        RecipeHelper.addBrewingRecipe(bottleIn, potionIn, ingredient, bottleOut, potionOut);
    }
}

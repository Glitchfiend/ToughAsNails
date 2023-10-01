/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.api.potion.TANPotions;
import toughasnails.core.ToughAsNails;
import toughasnails.crafting.WaterPurifierRecipe;

import java.util.function.Supplier;

public class ModCrafting
{
    public static void init()
    {
        registerRecipeSerializers();
    }

    private static void registerRecipeSerializers()
    {
        TANRecipeSerializers.WATER_PURIFYING = registerSerializer("water_purifying", WaterPurifierRecipe.Serializer::new);

        TANRecipeTypes.WATER_PURIFYING = registerRecipe("water_purifying", () -> new RecipeType<WaterPurifierRecipe>()
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
        addBrewingRecipe(Potions.AWKWARD, new ItemStack(Items.SNOWBALL), TANPotions.ICE_RESISTANCE.get());

        // Extended
        addBrewingRecipe(TANPotions.ICE_RESISTANCE.get(), new ItemStack(Items.REDSTONE), TANPotions.LONG_ICE_RESISTANCE.get());

        // Splash and lingering
        addPotionTransforms(TANPotions.ICE_RESISTANCE.get());
        addPotionTransforms(TANPotions.LONG_ICE_RESISTANCE.get());
    }

    public static RegistryObject<RecipeSerializer<?>> registerSerializer(String name, Supplier<RecipeSerializer<?>> serializer)
    {
        return ToughAsNails.RECIPE_SERIALIZER_REGISTER.register(name, serializer);
    }

    public static RegistryObject<RecipeType<?>> registerRecipe(String name, Supplier<RecipeType<?>> type)
    {
        return ToughAsNails.RECIPE_TYPE_REGISTER.register(name, type);
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

    private static void addBrewingRecipe(ItemStack inBottle, Potion inPotion, ItemStack ingredient, ItemStack outBottle, Potion outPotion)
    {
        BrewingRecipeRegistry.addRecipe(StrictNBTIngredient.of(PotionUtils.setPotion(inBottle, inPotion)), StrictNBTIngredient.of(ingredient), PotionUtils.setPotion(outBottle, outPotion));
    }
}

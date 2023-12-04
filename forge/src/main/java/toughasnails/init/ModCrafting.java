/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.IRegistryEventContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.ingredients.StrictNBTIngredient;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.api.potion.TANPotions;
import toughasnails.core.ToughAsNails;
import toughasnails.crafting.WaterPurifierRecipe;

public class ModCrafting
{
    public static void registerRecipeSerializers(IRegistryEventContext<RecipeSerializer<?>> context)
    {
        TANRecipeSerializers.WATER_PURIFYING = registerSerializer(context, "water_purifying", new WaterPurifierRecipe.Serializer());
    }

    public static void registerRecipeTypes(IRegistryEventContext<RecipeType<?>> context)
    {
        TANRecipeTypes.WATER_PURIFYING = registerRecipe(context, "water_purifying", new RecipeType<WaterPurifierRecipe>()
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

    private static RecipeSerializer<?> registerSerializer(IRegistryEventContext<RecipeSerializer<?>> context, String name, RecipeSerializer<?> serializer)
    {
        return context.register(new ResourceLocation(ToughAsNails.MOD_ID, name), serializer);
    }

    private static RecipeType<?> registerRecipe(IRegistryEventContext<RecipeType<?>> context, String name, RecipeType<?> type)
    {
        return context.register(new ResourceLocation(ToughAsNails.MOD_ID, name), type);
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

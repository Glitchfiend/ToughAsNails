/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.ingredients.StrictNBTIngredient;

public final class RecipeHelperImpl
{
    public static void addBrewingRecipe(ItemStack bottleIn, Potion potionIn, ItemStack ingredient, ItemStack bottleOut, Potion potionOut)
    {
        BrewingRecipeRegistry.addRecipe(StrictNBTIngredient.of(PotionUtils.setPotion(bottleIn, potionIn)), StrictNBTIngredient.of(ingredient), PotionUtils.setPotion(bottleOut, potionOut));
    }
}

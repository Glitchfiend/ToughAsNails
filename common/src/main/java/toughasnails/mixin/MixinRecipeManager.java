/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.crafting.WaterPurifierRecipe;
import toughasnails.init.ModRecipePropertySets;

import java.util.Map;
import java.util.Optional;

@Mixin(value = RecipeManager.class, remap = false)
public class MixinRecipeManager
{
    @Shadow private static Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> RECIPE_PROPERTY_SETS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onInit(CallbackInfo ci)
    {
        RECIPE_PROPERTY_SETS = ImmutableMap.<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor>builder().putAll(RECIPE_PROPERTY_SETS).put(ModRecipePropertySets.WATER_PURIFYING, recipe -> recipe instanceof WaterPurifierRecipe waterPurifierRecipe ? Optional.of(Ingredient.of(waterPurifierRecipe.input().getItem())) : Optional.empty()).build();
    }
}

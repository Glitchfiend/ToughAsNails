package toughasnails.mixin.glitchcore.impl;

import glitchcore.util.RecipeHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.ingredients.StrictNBTIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RecipeHelper.class, remap = false)
public abstract class MixinRecipeHelper
{
    @Overwrite
    public static void addBrewingRecipe(ItemStack bottleIn, Potion potionIn, ItemStack ingredient, ItemStack bottleOut, Potion potionOut)
    {
        BrewingRecipeRegistry.addRecipe(StrictNBTIngredient.of(PotionUtils.setPotion(bottleIn, potionIn)), StrictNBTIngredient.of(ingredient), PotionUtils.setPotion(bottleOut, potionOut));
    }
}

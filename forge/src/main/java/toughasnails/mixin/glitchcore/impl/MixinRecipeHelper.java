package toughasnails.mixin.glitchcore.impl;

import glitchcore.forge.util.EnvironmentImpl;
import glitchcore.forge.util.RecipeHelperImpl;
import glitchcore.util.Environment;
import glitchcore.util.RecipeHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RecipeHelper.class, remap = false)
public abstract class MixinRecipeHelper
{
    @Overwrite
    public static void addBrewingRecipe(ItemStack bottleIn, Potion potionIn, ItemStack ingredient, ItemStack bottleOut, Potion potionOut)
    {
        RecipeHelperImpl.addBrewingRecipe(bottleIn, potionIn, ingredient, bottleOut, potionOut);
    }
}

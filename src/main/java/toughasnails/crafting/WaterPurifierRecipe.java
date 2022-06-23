/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;

public class WaterPurifierRecipe implements Recipe<Container>
{
    protected final ResourceLocation id;
    protected final NBTIngredient ingredient;
    protected final ItemStack result;
    protected final int purifyTime;

    public WaterPurifierRecipe(ResourceLocation id, NBTIngredient ingredient, ItemStack result, int purifyTime)
    {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.purifyTime = purifyTime;
    }

    @Override
    public boolean matches(Container inv, Level worldIn)
    {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv)
    {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem()
    {
        return this.result;
    }

    @Override
    public ResourceLocation getId()
    {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return TANRecipeSerializers.WATER_PURIFYING.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return TANRecipeTypes.WATER_PURIFYING.get();
    }

    public int getPurifyTime()
    {
        return this.purifyTime;
    }

    public static class Serializer implements RecipeSerializer<WaterPurifierRecipe>
    {
        @Override
        public WaterPurifierRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            NBTIngredient ingredient = NBTIngredient.Serializer.INSTANCE.parse(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            int purifyTime = GsonHelper.getAsInt(json, "purifytime", 200);
            return new WaterPurifierRecipe(recipeId, ingredient, result, purifyTime);
        }

        @Override
        public WaterPurifierRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            NBTIngredient ingredient = (NBTIngredient)Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int purifyTime = buffer.readInt();
            return new WaterPurifierRecipe(recipeId, ingredient, result, purifyTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WaterPurifierRecipe recipe)
        {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.purifyTime);
        }
    }
}

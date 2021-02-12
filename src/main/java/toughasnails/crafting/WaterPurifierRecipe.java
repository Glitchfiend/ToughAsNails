/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.registries.ForgeRegistryEntry;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.core.ToughAsNails;

public class WaterPurifierRecipe implements IRecipe<IInventory>
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
    public boolean matches(IInventory inv, World worldIn)
    {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory inv)
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
    public IRecipeSerializer<?> getSerializer()
    {
        return TANRecipeSerializers.WATER_PURIFYING;
    }

    @Override
    public IRecipeType<?> getType()
    {
        return TANRecipeTypes.WATER_PURIFYING;
    }

    public int getPurifyTime()
    {
        return this.purifyTime;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WaterPurifierRecipe>
    {
        @Override
        public WaterPurifierRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            NBTIngredient ingredient = NBTIngredient.Serializer.INSTANCE.parse(JSONUtils.getAsJsonObject(json, "ingredient"));
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
            int purifyTime = JSONUtils.getAsInt(json, "purifytime", 200);
            return new WaterPurifierRecipe(recipeId, ingredient, result, purifyTime);
        }

        @Override
        public WaterPurifierRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
        {
            NBTIngredient ingredient = (NBTIngredient)Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int purifyTime = buffer.readInt();
            return new WaterPurifierRecipe(recipeId, ingredient, result, purifyTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, WaterPurifierRecipe recipe)
        {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.purifyTime);
        }
    }
}

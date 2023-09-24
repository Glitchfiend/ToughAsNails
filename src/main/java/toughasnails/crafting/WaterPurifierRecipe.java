/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.crafting;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;

public class WaterPurifierRecipe implements Recipe<Container>
{
    protected final StrictNBTIngredient ingredient;
    protected final ItemStack result;
    protected final int purifyTime;

    public WaterPurifierRecipe(StrictNBTIngredient ingredient, ItemStack result, int purifyTime)
    {
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
    public ItemStack assemble(Container inv, RegistryAccess registryAccess)
    {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess)
    {
        return this.result;
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
        private static final Codec<WaterPurifierRecipe> CODEC = RecordCodecBuilder.create((builder) -> {
            return builder.group(StrictNBTIngredient.CODEC.fieldOf("ingredient").forGetter((p_296920_) -> {
                return p_296920_.ingredient;
            }), ItemStack.CODEC.fieldOf("result").forGetter((p_296923_) -> {
                return p_296923_.result;
            }), Codec.INT.fieldOf("purifytime").orElse(200).forGetter((p_296919_) -> {
                return p_296919_.purifyTime;
            })).apply(builder, WaterPurifierRecipe::new);
        });

        @Override
        public WaterPurifierRecipe fromNetwork(FriendlyByteBuf buffer)
        {
            StrictNBTIngredient ingredient = (StrictNBTIngredient)Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int purifyTime = buffer.readInt();
            return new WaterPurifierRecipe(ingredient, result, purifyTime);
        }

        @Override
        public Codec<WaterPurifierRecipe> codec()
        {
            return CODEC;
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

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;

public class WaterPurifierRecipe implements Recipe<Container>
{
    protected final ItemStack input;
    protected final ItemStack result;
    protected final int purifyTime;

    public WaterPurifierRecipe(ItemStack input, ItemStack result, int purifyTime)
    {
        this.input = input;
        this.result = result;
        this.purifyTime = purifyTime;
    }

    @Override
    public boolean matches(Container inv, Level worldIn)
    {
        if (this.input == null)
            return false;

        ItemStack containerInput = inv.getItem(0);

        return this.input.getItem() == containerInput.getItem() && this.input.getDamageValue() == containerInput.getDamageValue() && this.input.areShareTagsEqual(containerInput);
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
            return builder.group(ItemStack.CODEC.fieldOf("input").forGetter((p_296920_) -> {
                return p_296920_.input;
            }), ItemStack.CODEC.fieldOf("result").forGetter((p_296923_) -> {
                return p_296923_.result;
            }), Codec.INT.fieldOf("purifytime").orElse(200).forGetter((p_296919_) -> {
                return p_296919_.purifyTime;
            })).apply(builder, WaterPurifierRecipe::new);
        });

        @Override
        public WaterPurifierRecipe fromNetwork(FriendlyByteBuf buffer)
        {
            ItemStack input = buffer.readItem();
            ItemStack result = buffer.readItem();
            int purifyTime = buffer.readInt();
            return new WaterPurifierRecipe(input, result, purifyTime);
        }

        @Override
        public Codec<WaterPurifierRecipe> codec()
        {
            return CODEC;
        }


        @Override
        public void toNetwork(FriendlyByteBuf buffer, WaterPurifierRecipe recipe)
        {
            buffer.writeItem(recipe.input);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.purifyTime);
        }
    }
}

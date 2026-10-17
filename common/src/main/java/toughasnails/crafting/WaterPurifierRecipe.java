/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import toughasnails.api.crafting.TANRecipeBookCategories;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;

import javax.annotation.Nullable;

public class WaterPurifierRecipe implements Recipe<SingleRecipeInput>
{
    protected final ItemStackTemplate inputData;
    protected final ItemStackTemplate resultData;
    protected final int purifyTime;
    @Nullable
    private ItemStack cachedInput;
    @Nullable
    private ItemStack cachedResult;
    @Nullable
    private PlacementInfo placementInfo;

    public WaterPurifierRecipe(ItemStackTemplate inputData, ItemStackTemplate resultData, int purifyTime)
    {
        this.inputData = inputData;
        this.resultData = resultData;
        this.purifyTime = purifyTime;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level var2)
    {
        ItemStack containerInput = input.getItem(0);
        return ItemStack.isSameItemSameComponents(this.input(), containerInput) && this.input().getDamageValue() == containerInput.getDamageValue();
    }

    @Override
    public ItemStack assemble(SingleRecipeInput var1)
    {
        return this.result().copy();
    }

    @Override
    public boolean showNotification()
    {
        return true;
    }

    @Override
    public String group()
    {
        return "";
    }

    public ItemStack input()
    {
        if (this.cachedInput == null)
        {
            this.cachedInput = this.inputData.create();
        }

        return this.cachedInput;
    }

    protected ItemStack result()
    {
        if (this.cachedResult == null)
        {
            this.cachedResult = this.resultData.create();
        }

        return this.cachedResult;
    }

    @Override
    public PlacementInfo placementInfo()
    {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(Ingredient.of(this.inputData.item().value()));
        }

        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory()
    {
        return TANRecipeBookCategories.WATER_PURIFYING;
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer()
    {
        return TANRecipeSerializers.WATER_PURIFYING;
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType()
    {
        return TANRecipeTypes.WATER_PURIFYING;
    }

    public int getPurifyTime()
    {
        return this.purifyTime;
    }

    public static final MapCodec<WaterPurifierRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> {
        return builder.group(ItemStackTemplate.CODEC.fieldOf("input").forGetter((p_296920_) -> {
            return p_296920_.inputData;
        }), ItemStackTemplate.CODEC.fieldOf("result").forGetter((p_296923_) -> {
            return p_296923_.resultData;
        }), Codec.INT.fieldOf("purifytime").orElse(200).forGetter((p_296919_) -> {
            return p_296919_.purifyTime;
        })).apply(builder, WaterPurifierRecipe::new);
    });

    public static final StreamCodec<RegistryFriendlyByteBuf, WaterPurifierRecipe> STREAM_CODEC = StreamCodec.of(WaterPurifierRecipe::toNetwork, WaterPurifierRecipe::fromNetwork);

    private static WaterPurifierRecipe fromNetwork(RegistryFriendlyByteBuf buffer)
    {
        ItemStackTemplate input = ItemStackTemplate.STREAM_CODEC.decode(buffer);
        ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
        int purifyTime = buffer.readInt();
        return new WaterPurifierRecipe(input, result, purifyTime);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, WaterPurifierRecipe recipe)
    {
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.inputData);
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.resultData);
        buffer.writeInt(recipe.purifyTime);
    }
}

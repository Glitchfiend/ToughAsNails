/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/

package toughasnails.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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
import toughasnails.api.block.TANBlocks;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.util.serialization.ItemStackDeserializer;

public class WaterPurifierRecipe implements Recipe<Container> {
  protected final ResourceLocation id;

  protected final ItemStack input;
  protected final ItemStack result;
  protected final int purifyTime;

  public WaterPurifierRecipe(ResourceLocation resourceLocation, ItemStack input, ItemStack itemStack, int i) {
    this.id = resourceLocation;
    this.input = input;
    this.result = itemStack;
    this.purifyTime = i;
  }

  @Override
  public boolean matches(Container inv, Level worldIn)
  {
    if (this.input == null)
      return false;

    ItemStack containerInput = inv.getItem(0);
    return ItemStack.isSameItemSameTags(this.input, containerInput) && this.input.getDamageValue() == containerInput.getDamageValue();
  }

  @Override
  public ItemStack assemble(Container container, RegistryAccess registryAccess) {
    return this.result.copy();
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return true;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    NonNullList<Ingredient> nonNullList = NonNullList.create();
    nonNullList.add(Ingredient.of(input));
    return nonNullList;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return this.result;
  }

  @Override
  public ResourceLocation getId() {
    return this.id;
  }

  @Override
  public RecipeType<?> getType() {
    return TANRecipeTypes.WATER_PURIFYING;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(TANBlocks.WATER_PURIFIER);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return TANRecipeSerializers.WATER_PURIFYING;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  // custom methods

  public int getPurifyTime() {
    return this.purifyTime;
  }

  public static class Serializer implements RecipeSerializer<WaterPurifierRecipe>
  {
    @Override
    public WaterPurifierRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
      ItemStack input = ItemStackDeserializer.deserialize(jsonObject.getAsJsonObject("input"));
      ItemStack result = ItemStackDeserializer.deserialize(jsonObject.getAsJsonObject("result"));
      int purifyTime = GsonHelper.getAsInt(jsonObject, "purifytime");

      return new WaterPurifierRecipe(resourceLocation, input, result, purifyTime);
    }

    @Override
    public WaterPurifierRecipe fromNetwork(ResourceLocation resourceLocation,
        FriendlyByteBuf buffer) {
      ItemStack input = buffer.readItem();
      ItemStack result = buffer.readItem();
      int purifyTime = buffer.readInt();
      return new WaterPurifierRecipe(resourceLocation, input, result, purifyTime);
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

package toughasnails.forge.datagen.recipes;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.TANAPI;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.crafting.WaterPurifierRecipe;
import toughasnails.util.serialization.ItemStackDeserializer;

import java.util.function.Consumer;

public class WaterPurifierRecipeBuilder
{
    private final ItemStack input;
    private final ItemStack result;
    private final int purifyTime;

    private WaterPurifierRecipeBuilder(ItemStack input, ItemStack result, int purifyTime)
    {
        this.input = input;
        this.result = result;
        this.purifyTime = purifyTime;
    }

    public static WaterPurifierRecipeBuilder waterPurifier(ItemStack input, ItemStack result, int purifyTime)
    {
        return new WaterPurifierRecipeBuilder(input, result, purifyTime);
    }

    public void save(Consumer<FinishedRecipe> output, ResourceLocation location, ItemStack input, ItemStack result, int purifyTime)
    {
        output.accept(new Result(location, input, result, purifyTime));
    }

    public static class Result implements FinishedRecipe {
        public static final RecipeSerializer<?> TYPE = RecipeSerializer.register(TANAPI.MOD_ID + ":water_purifying", new WaterPurifierRecipe.Serializer());
        private final ResourceLocation id;
        private final ItemStack input;
        private final ItemStack result;
        private final int purifyTime;
        public Result(ResourceLocation resourceLocation, ItemStack input, ItemStack result, int purifyTime) {
            this.id = resourceLocation;
            this.input = input;
            this.result = result;
            this.purifyTime = purifyTime;
        }
        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            jsonObject.addProperty("purifytime", purifyTime);
            jsonObject.add("input", ItemStackDeserializer.serialize(input));
            jsonObject.add("result", ItemStackDeserializer.serialize(result));
        }
        @Override
        public ResourceLocation getId() {
            return id;
        }
        @Override
        public RecipeSerializer<?> getType() {
            return TYPE;
        }
        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }
        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

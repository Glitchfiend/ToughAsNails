package toughasnails.datagen.recipes;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import toughasnails.api.crafting.TANRecipeSerializers;

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

    public void save(RecipeOutput output, String name)
    {
        this.save(output, new ResourceLocation(name));
    }

    public void save(RecipeOutput output, ResourceLocation location)
    {
        output.accept(new WaterPurifierRecipeBuilder.Result(location, TANRecipeSerializers.WATER_PURIFYING.get(), this.input, this.result, this.purifyTime));
    }

    public record Result(ResourceLocation id, RecipeSerializer<?> type, ItemStack input, ItemStack result, int purifyTime) implements FinishedRecipe {
        public void serializeRecipeData(JsonObject json) {
            json.add("input", Util.getOrThrow(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.input), IllegalStateException::new));
            json.add("result", Util.getOrThrow(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.result), IllegalStateException::new));
            json.addProperty("purifytime", this.purifyTime);
        }

        @Override
        public ResourceLocation id()
        {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> type()
        {
            return this.type;
        }

        @Override
        public AdvancementHolder advancement()
        {
            return null;
        }
    }
}

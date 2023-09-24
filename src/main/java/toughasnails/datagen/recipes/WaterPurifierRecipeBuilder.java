package toughasnails.datagen.recipes;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import toughasnails.api.crafting.TANRecipeSerializers;

public class WaterPurifierRecipeBuilder
{
    private final StrictNBTIngredient ingredient;
    private final ItemStack result;
    private final int purifyTime;

    private WaterPurifierRecipeBuilder(StrictNBTIngredient ingredient, ItemStack result, int purifyTime)
    {
        this.ingredient = ingredient;
        this.result = result;
        this.purifyTime = purifyTime;
    }

    public static WaterPurifierRecipeBuilder waterPurifier(StrictNBTIngredient ingredient, ItemStack result, int purifyTime)
    {
        return new WaterPurifierRecipeBuilder(ingredient, result, purifyTime);
    }

    public void save(RecipeOutput output, String name)
    {
        this.save(output, new ResourceLocation(name));
    }

    public void save(RecipeOutput output, ResourceLocation location)
    {
        output.accept(new WaterPurifierRecipeBuilder.Result(location, TANRecipeSerializers.WATER_PURIFYING.get(), this.ingredient, this.result, this.purifyTime));
    }

    public record Result(ResourceLocation id, RecipeSerializer<?> type, StrictNBTIngredient ingredient, ItemStack result, int purifyTime) implements FinishedRecipe {
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", Util.getOrThrow(StrictNBTIngredient.CODEC.encodeStart(JsonOps.INSTANCE, this.ingredient), IllegalStateException::new));
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

package toughasnails.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.datagen.recipes.WaterPurifierRecipeBuilder;

public class TANRecipeProvider extends RecipeProvider
{
    public TANRecipeProvider(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        // Water bottle
        waterPurifier(output, new ItemStack(TANItems.DIRTY_WATER_BOTTLE.get()), PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), 400);
        waterPurifier(output, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), new ItemStack(TANItems.PURIFIED_WATER_BOTTLE.get()), 200);

        // Canteens
        waterPurifier(output, new ItemStack(TANItems.DIRTY_WATER_CANTEEN.get()), new ItemStack(TANItems.WATER_CANTEEN.get()), 400);
        waterPurifier(output, new ItemStack(TANItems.WATER_CANTEEN.get()), new ItemStack(TANItems.PURIFIED_WATER_CANTEEN.get()), 200);
    }

    public static void waterPurifier(RecipeOutput output, ItemStack input, ItemStack result, int purifyTime)
    {
        WaterPurifierRecipeBuilder.waterPurifier(input, result, purifyTime).save(output, new ResourceLocation(ToughAsNails.MOD_ID, getItemName(result.getItem())));
    }
}

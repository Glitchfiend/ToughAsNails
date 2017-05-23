package toughasnails.util;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class PotionBrewingRecipe extends BrewingRecipe
{
	
	private ItemStack input;

	public PotionBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output)
	{
		super(input, ingredient, output);
		this.input = input;
	}

    @Override
    public boolean isInput(@Nonnull ItemStack stack)
    {
        return super.isInput(stack) && ItemStack.areItemStackTagsEqual(input, stack);
    }
}

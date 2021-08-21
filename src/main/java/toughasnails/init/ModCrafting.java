/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.api.potion.TANPotions;
import toughasnails.core.ToughAsNails;
import toughasnails.crafting.WaterPurifierRecipe;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCrafting
{
    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event)
    {
        register("water_purifying", new WaterPurifierRecipe.Serializer());

        TANRecipeTypes.WATER_PURIFYING = register("water_purifying", new RecipeType<WaterPurifierRecipe>()
        {
            @Override
            public String toString()
            {
                return "water_purifying";
            }
        });

        // Brewing
        // Base
        addBrewingRecipe(Potions.AWKWARD, new ItemStack(Items.SNOWBALL), TANPotions.ICE_RESISTANCE);

        // Extended
        addBrewingRecipe(TANPotions.ICE_RESISTANCE, new ItemStack(Items.REDSTONE), TANPotions.LONG_ICE_RESISTANCE);

        // Splash and lingering
        addPotionTransforms(TANPotions.ICE_RESISTANCE);
        addPotionTransforms(TANPotions.LONG_ICE_RESISTANCE);
    }

    public static void register(String name, RecipeSerializer serializer)
    {
        serializer.setRegistryName(new ResourceLocation(ToughAsNails.MOD_ID, name));
        ForgeRegistries.RECIPE_SERIALIZERS.register(serializer);
    }

    public static <T extends Recipe<?>> RecipeType<T> register(String name, RecipeType type)
    {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ToughAsNails.MOD_ID, name), type);
    }

    private static void addBrewingRecipe(Potion input, ItemStack ingredient, Potion output)
    {
        addBrewingRecipe(new ItemStack(Items.POTION), input, ingredient, new ItemStack(Items.POTION), output);
        addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), input, ingredient, new ItemStack(Items.SPLASH_POTION), output);
        addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), input, ingredient, new ItemStack(Items.LINGERING_POTION), output);
    }

    private static void addPotionTransforms(Potion potion)
    {
        // Splash
        addBrewingRecipe(new ItemStack(Items.POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);
        addBrewingRecipe(new ItemStack(Items.LINGERING_POTION), potion, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPLASH_POTION), potion);

        // Lingering
        addBrewingRecipe(new ItemStack(Items.POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
        addBrewingRecipe(new ItemStack(Items.SPLASH_POTION), potion, new ItemStack(Items.DRAGON_BREATH), new ItemStack(Items.LINGERING_POTION), potion);
    }

    private static void addBrewingRecipe(ItemStack inBottle, Potion inPotion, ItemStack ingredient, ItemStack outBottle, Potion outPotion)
    {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(inBottle, inPotion)), Ingredient.of(ingredient), PotionUtils.setPotion(outBottle, outPotion));
    }
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.api.crafting.TANRecipeTypes;
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
}

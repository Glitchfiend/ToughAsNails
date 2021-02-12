/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        register("water_purifying", new WaterPurifierRecipe.Serializer());

        TANRecipeTypes.WATER_PURIFYING = register("water_purifying", new IRecipeType<WaterPurifierRecipe>()
        {
            @Override
            public String toString()
            {
                return "water_purifying";
            }
        });
    }

    public static void register(String name, IRecipeSerializer serializer)
    {
        serializer.setRegistryName(new ResourceLocation(ToughAsNails.MOD_ID, name));
        ForgeRegistries.RECIPE_SERIALIZERS.register(serializer);
    }

    public static <T extends IRecipe<?>> IRecipeType<T> register(String name, IRecipeType type)
    {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ToughAsNails.MOD_ID, name), type);
    }
}

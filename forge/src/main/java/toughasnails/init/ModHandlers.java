/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.forge.GlitchCoreForge;
import glitchcore.util.RegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.thirst.ThirstHandler;

public class ModHandlers
{
    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(new ThirstHandler());
        MinecraftForge.EVENT_BUS.register(new TemperatureHandler());

        var regHelper = RegistryHelper.create();
        regHelper.addRegistrar(Registries.BLOCK, ModBlocks::registerBlocks);
        regHelper.addRegistrar(Registries.ITEM, ModItems::registerItems);
        regHelper.addRegistrar(Registries.MENU, ModContainerTypes::registerContainers);
        regHelper.addRegistrar(Registries.BLOCK_ENTITY_TYPE, ModBlockEntities::registerBlockEntities);
        regHelper.addRegistrar(Registries.RECIPE_SERIALIZER, ModCrafting::registerRecipeSerializers);
        regHelper.addRegistrar(Registries.RECIPE_TYPE, ModCrafting::registerRecipeTypes);
        regHelper.addRegistrar(Registries.ENCHANTMENT, ModEnchantments::registerEnchantments);
        regHelper.addRegistrar(Registries.MOB_EFFECT, ModPotions::registerEffects);
        regHelper.addRegistrar(Registries.POTION, ModPotions::registerPotions);
        regHelper.addRegistrar(Registries.CREATIVE_MODE_TAB, ModCreativeTab::registerCreativeTabs);

        GlitchCoreForge.prepareEventHandlers(bus);
    }
}

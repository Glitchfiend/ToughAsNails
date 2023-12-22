/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import glitchcore.event.EventManager;
import glitchcore.util.Environment;
import glitchcore.util.RegistryHelper;
import net.minecraft.core.registries.Registries;
import toughasnails.init.*;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.temperature.TemperatureOverlayRenderer;
import toughasnails.thirst.ThirstOverlayRenderer;

public class ToughAsNails
{
    public static void init()
    {
        ModConfig.init();
        addRegistrars();
        addHandlers();
        ModPackets.init();
        ModApi.init();
    }

    private static void addRegistrars()
    {
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
    }

    private static void addHandlers()
    {
        // Temperature handlers
        EventManager.addListener(TemperatureOverlayRenderer::onClientTick);
        EventManager.addListener(TemperatureOverlayRenderer::onBeginRenderFood);
        EventManager.addListener(TemperatureOverlayRenderer::onBeginRenderFrostbite);
        EventManager.addListener(TemperatureHandler::onChangeDimension);

        // Thirst handlers
        EventManager.addListener(ThirstOverlayRenderer::onClientTick);
        EventManager.addListener(ThirstOverlayRenderer::onBeginRenderAir);
    }
}

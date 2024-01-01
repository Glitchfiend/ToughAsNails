/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import glitchcore.event.EventManager;
import glitchcore.util.Environment;
import glitchcore.util.RegistryHelper;
import net.minecraft.core.registries.Registries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toughasnails.api.TANAPI;
import toughasnails.client.handler.TooltipHandler;
import toughasnails.init.*;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.temperature.TemperatureOverlayRenderer;
import toughasnails.thirst.ThirstHandler;
import toughasnails.thirst.ThirstOverlayRenderer;

public class ToughAsNails
{
    public static final String MOD_ID = TANAPI.MOD_ID;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init()
    {
        ModConfig.init();
        ModTags.init();
        addRegistrars();
        addHandlers();
        ModPackets.init();
        ModApi.init();
    }

    public static void setupClient()
    {
        ModBlocks.registerRenderers();
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
        EventManager.addListener(TemperatureHandler::onChangeDimension);
        EventManager.addListener(TemperatureHandler::onItemUseFinish);

        // Thirst handlers
        EventManager.addListener(ThirstHandler::onChangeDimension);
        EventManager.addListener(ThirstHandler::onItemUseFinish);
        EventManager.addListener(ThirstHandler::onPlayerUseItem);

        if (Environment.isClient())
        {
            addClientHandlers();
        }
    }

    private static void addClientHandlers()
    {
        // Temperature
        EventManager.addListener(TemperatureOverlayRenderer::onClientTick);
        EventManager.addListener(TemperatureOverlayRenderer::onBeginRenderFood);
        EventManager.addListener(TemperatureOverlayRenderer::onBeginRenderFrostbite);

        // Thirst
        EventManager.addListener(ThirstOverlayRenderer::onClientTick);
        EventManager.addListener(ThirstOverlayRenderer::onBeginRenderAir);
        EventManager.addListener(ThirstHandler::onUseEmpty);
        EventManager.addListener(ThirstHandler::onClientTick);

        // Coloring
        EventManager.addListener(ModBlocks::registerBlockColors);
        EventManager.addListener(ModItems::registerItemColors);

        // Tooltips
        EventManager.addListener(TooltipHandler::onTooltip);
        EventManager.addListener(TooltipHandler::onRenderTooltip);
    }
}

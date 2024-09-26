/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import glitchcore.event.EventManager;
import glitchcore.util.Environment;
import glitchcore.util.RegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toughasnails.api.TANAPI;
import toughasnails.client.handler.KeyHandler;
import toughasnails.client.handler.LevelRenderHandler;
import toughasnails.client.handler.TooltipHandler;
import toughasnails.init.ModApi;
import toughasnails.init.ModBlockEntities;
import toughasnails.init.ModBlocks;
import toughasnails.init.ModClient;
import toughasnails.init.ModCompatibility;
import toughasnails.init.ModConfig;
import toughasnails.init.ModContainerTypes;
import toughasnails.init.ModCrafting;
import toughasnails.init.ModCreativeTab;
import toughasnails.init.ModEnchantments;
import toughasnails.init.ModItems;
import toughasnails.init.ModPackets;
import toughasnails.init.ModParticles;
import toughasnails.init.ModPotions;
import toughasnails.init.ModTags;
import toughasnails.init.ModVillages;
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
        ModCompatibility.init();
    }

    public static void setupClient()
    {
        ModClient.setupRenderTypes();
    }

    public static void onServerAboutToStart(MinecraftServer server)
    {
        ModVillages.addBuildings(server.registryAccess());
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
        regHelper.addRegistrar(Registries.PARTICLE_TYPE, ModParticles::registerParticles);
        regHelper.addRegistrar(Registries.POINT_OF_INTEREST_TYPE, ModVillages::registerPointsOfInterest);
        regHelper.addRegistrar(Registries.VILLAGER_PROFESSION, ModVillages::registerProfessions);
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
        EventManager.addListener(ThirstHandler::onUseBlock);

        // Misc handlers
        EventManager.addListener(ModVillages::addVillagerTrades);
        EventManager.addListener(ModVillages::addWanderingVillagerTrades);

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
        EventManager.addListener(LevelRenderHandler::onLevelRender);

        // Thirst
        EventManager.addListener(ThirstOverlayRenderer::onClientTick);
        EventManager.addListener(ThirstOverlayRenderer::onBeginRenderAir);
        EventManager.addListener(ThirstHandler::onUseEmpty);
        EventManager.addListener(ThirstHandler::onClientTick);

        // Coloring
        EventManager.addListener(ModClient::registerBlockColors);
        EventManager.addListener(ModClient::registerItemColors);

        // Particles
        EventManager.addListener(ModClient::registerParticleSprites);

        // Tooltips
        EventManager.addListener(TooltipHandler::onTooltip);
        EventManager.addListener(TooltipHandler::onRenderTooltip);

        // Debug
        EventManager.addListener(KeyHandler::onKeyPress);
    }
}

/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerData;
import toughasnails.core.ToughAsNails;
import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.RegistryEvent;
import toughasnails.glitch.event.TickEvent;
import toughasnails.glitch.event.village.VillagerTradesEvent;
import toughasnails.glitch.event.village.WandererTradesEvent;

public class ToughAsNailsFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ToughAsNails.init();
        postRegisterEvents();

        var wandererTradesEvent = new WandererTradesEvent();
        EventManager.fire(wandererTradesEvent);

        TradeOfferHelper.registerWanderingTraderOffers(1, (list) -> {
            list.addAll(wandererTradesEvent.getGenericTrades());
        });

        TradeOfferHelper.registerWanderingTraderOffers(2, (list) -> {
            list.addAll(wandererTradesEvent.getRareTrades());
        });
        BuiltInRegistries.VILLAGER_PROFESSION.forEach(profession -> {
            for (int level = VillagerData.MIN_VILLAGER_LEVEL; level <= VillagerData.MAX_VILLAGER_LEVEL; level++)
            {
                final int finalLevel = level;
                TradeOfferHelper.registerVillagerOffers(profession, level, trades -> EventManager.fire(new VillagerTradesEvent(profession, finalLevel, trades)));
            }
        });
        ServerTickEvents.START_WORLD_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.START, level));
        });

        ServerTickEvents.END_WORLD_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.END, level));
        });
        ServerLifecycleEvents.SERVER_STARTING.register(ToughAsNails::onServerAboutToStart);
    }

    private static void postRegisterEvents()
    {
        // We use LOADERS to ensure objects are registered at the correct time relative to each other
        for (ResourceLocation registryName : BuiltInRegistries.LOADERS.keySet())
        {
            ResourceKey<? extends Registry<?>> registryKey = ResourceKey.createRegistryKey(registryName);
            Registry<?> registry = BuiltInRegistries.REGISTRY.get(registryName);
            EventManager.fire(new RegistryEvent(registryKey, (location, value) -> Registry.register((Registry<? super Object>)registry, location, value)));
        }
    }
}

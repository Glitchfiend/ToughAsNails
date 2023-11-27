/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import com.google.common.collect.ImmutableMap;
import glitchcore.event.Event;
import glitchcore.event.Events;
import glitchcore.event.IRegistryEventContext;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Objects;

public class RegistryEventHandler
{
    private static final ImmutableMap<ResourceKey<? extends Registry<?>>, Event<? extends IRegistryEventContext<?>>> EVENT_MAPPINGS;

    public static void setup(IEventBus modEventBus)
    {
        if (EVENT_MAPPINGS.values().stream().anyMatch(Event::hasListeners))
        {
            modEventBus.addListener(RegistryEventHandler::onRegister);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void onRegister(RegisterEvent event)
    {
        var key = event.getRegistryKey();
        if (!EVENT_MAPPINGS.containsKey(key))
            return;

        var gcEvent = (Event<IRegistryEventContext<?>>)Objects.requireNonNull(EVENT_MAPPINGS.get(key));
        gcEvent.fire(((location, value) -> {
            event.register((ResourceKey)key, location, () -> value);
            return value;
        }));
    }

    static
    {
        EVENT_MAPPINGS = new ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, Event<? extends IRegistryEventContext<?>>>()
                .put(Registries.BLOCK, Events.BLOCK_REGISTRY_EVENT)
                .put(Registries.ITEM, Events.ITEM_REGISTRY_EVENT).build();
    }
}

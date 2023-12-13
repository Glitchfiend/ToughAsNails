/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.event.Event;
import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RegistryEventHandler
{
    public static void setup(IEventBus modEventBus)
    {
        modEventBus.addListener(RegistryEventHandler::onRegister);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void onRegister(RegisterEvent forgeEvent)
    {
        var registryKey = forgeEvent.getRegistryKey();
        EventManager.fire(new RegistryEvent(registryKey, (location, value) -> forgeEvent.register((ResourceKey<? extends Registry<Object>>)registryKey, location, () -> value)));
    }
}

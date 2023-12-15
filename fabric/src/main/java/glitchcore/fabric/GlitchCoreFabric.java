/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric;

import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;

public class GlitchCoreFabric
{
    public static void init()
    {
        postRegisterEvents();
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

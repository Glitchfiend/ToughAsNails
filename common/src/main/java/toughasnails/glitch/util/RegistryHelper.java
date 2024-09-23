/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.util;

import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.RegistryEvent;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RegistryHelper implements Consumer<RegistryEvent>
{
  private final Multimap<ResourceKey<? extends Registry<?>>, Registrar<?>> registrars = ArrayListMultimap.create();

  private RegistryHelper()
  {
    // Register self as an event handler
    EventManager.addListener(this);
  }

  public static RegistryHelper create()
  {
    return new RegistryHelper();
  }

  public <T> void addRegistrar(ResourceKey<? extends Registry<T>> registry, Registrar<T> registrar)
  {
    this.registrars.put(registry, registrar);
  }

  @Override
  public void accept(RegistryEvent registryEvent)
  {
    this.registrars.get(registryEvent.getRegistryKey()).forEach(registrar ->
    {
      ((Registrar<?>)registrar).registerAll(registryEvent::register);
    });
  }

  public interface Registrar<T>
  {
    void registerAll(BiConsumer<ResourceLocation, T> register);
  }
}

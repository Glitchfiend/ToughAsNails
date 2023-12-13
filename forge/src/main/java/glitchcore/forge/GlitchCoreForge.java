/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge;

import glitchcore.event.Event;
import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import glitchcore.forge.handlers.RegistryEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;

public class GlitchCoreForge
{
    public static void prepareEventHandlers(IEventBus modEventBus)
    {
        for (Class<? extends Event> eventClass : EventManager.getRequiredEvents())
        {
            if (eventClass.equals(RegistryEvent.class))
                RegistryEventHandler.setup(modEventBus);
        }
    }
}

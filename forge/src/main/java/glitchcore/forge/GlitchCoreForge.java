/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge;

import glitchcore.forge.handlers.RegistryEventHandler;
import net.minecraftforge.eventbus.api.IEventBus;

public class GlitchCoreForge
{
    public static void prepareEventHandlers(IEventBus modEventBus)
    {
        RegistryEventHandler.setup(modEventBus);
    }
}

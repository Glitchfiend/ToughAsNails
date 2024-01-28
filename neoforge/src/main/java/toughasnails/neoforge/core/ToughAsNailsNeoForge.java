/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.core;

import glitchcore.neoforge.GlitchCoreNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import toughasnails.core.ToughAsNails;

@Mod(value = ToughAsNails.MOD_ID)
public class ToughAsNailsNeoForge
{
    public ToughAsNailsNeoForge(IEventBus bus)
    {
        bus.addListener(this::clientSetup);
        NeoForge.EVENT_BUS.addListener(this::serverAboutToStart);

        ToughAsNails.init();
        GlitchCoreNeoForge.prepareModEventHandlers(bus);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(ToughAsNails::setupClient);
    }

    private void serverAboutToStart(final ServerStartingEvent event)
    {
        ToughAsNails.onServerAboutToStart(event.getServer());
    }
}

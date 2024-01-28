/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.core;

import glitchcore.forge.GlitchCoreForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import toughasnails.core.ToughAsNails;

@Mod(value = ToughAsNails.MOD_ID)
public class ToughAsNailsForge
{
    public ToughAsNailsForge()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);

        ToughAsNails.init();
        GlitchCoreForge.prepareModEventHandlers(bus);
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

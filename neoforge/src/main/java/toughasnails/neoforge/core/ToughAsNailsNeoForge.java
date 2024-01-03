/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.core;

import glitchcore.neoforge.GlitchCoreNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import toughasnails.core.ToughAsNails;

@Mod(value = ToughAsNails.MOD_ID)
public class ToughAsNailsNeoForge
{
    public ToughAsNailsNeoForge()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);

        ToughAsNails.init();
        GlitchCoreNeoForge.prepareModEventHandlers(bus);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(ToughAsNails::setupClient);
    }
}

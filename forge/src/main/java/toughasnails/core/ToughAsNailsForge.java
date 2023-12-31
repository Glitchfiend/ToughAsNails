/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import glitchcore.forge.GlitchCoreForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import toughasnails.init.*;

@Mod(value = ToughAsNails.MOD_ID)
public class ToughAsNailsForge
{
    public ToughAsNailsForge()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::loadComplete);
        bus.addListener(this::clientSetup);

        ToughAsNails.init();
        GlitchCoreForge.prepareModEventHandlers(bus);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(ToughAsNails::setupClient);
    }

    private void loadComplete(final FMLLoadCompleteEvent event)
    {
        event.enqueueWork(ModCompatibility::init);
    }
}

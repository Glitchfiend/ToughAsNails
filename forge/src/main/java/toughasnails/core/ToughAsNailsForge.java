/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import glitchcore.forge.GlitchCoreForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toughasnails.init.*;

@Mod(value = ToughAsNailsForge.MOD_ID)
public class ToughAsNailsForge
{
    public static final String MOD_ID = "toughasnails";


    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public ToughAsNailsForge()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::loadComplete);

        ToughAsNails.init();
        GlitchCoreForge.prepareModEventHandlers(bus);
    }

    private void loadComplete(final FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
            ModCrafting.registerPotionRecipes();
            ModCompatibility.init();
        });
    }
}

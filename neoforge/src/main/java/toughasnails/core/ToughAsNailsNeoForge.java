/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import glitchcore.neoforge.GlitchCoreNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import toughasnails.api.TANAPI;
import toughasnails.init.*;

@Mod(value = TANAPI.MOD_ID)
public class ToughAsNailsNeoForge
{
    public ToughAsNailsNeoForge()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ToughAsNails.init();
        GlitchCoreNeoForge.prepareModEventHandlers(bus);
    }
}

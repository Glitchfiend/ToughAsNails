/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.core;

import glitchcore.fabric.GlitchCoreInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import toughasnails.core.ToughAsNails;

public class ToughAsNailsFabric implements GlitchCoreInitializer
{
    @Override
    public void onInitialize()
    {
        ToughAsNails.init();

        ServerLifecycleEvents.SERVER_STARTING.register(ToughAsNails::onServerAboutToStart);
    }

    @Override
    public void onInitializeClient()
    {
        ToughAsNails.setupClient();
    }
}

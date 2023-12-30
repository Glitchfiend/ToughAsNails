/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.core;

import glitchcore.fabric.GlitchCoreFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class ToughAsNailsFabric implements ModInitializer
{
    /** Fabric TODOs
     * Fix brewing recipes
     * Add config synchronization for thirst/temperature
     * Fix temperatureModifierOrder
      */

    @Override
    public void onInitialize()
    {
        ToughAsNails.init();
        GlitchCoreFabric.prepareEvents();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ToughAsNails.initClient();
        });
    }
}

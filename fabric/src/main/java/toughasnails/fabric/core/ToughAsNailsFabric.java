/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.core;

import glitchcore.fabric.GlitchCoreInitializer;
import toughasnails.core.ToughAsNails;

public class ToughAsNailsFabric implements GlitchCoreInitializer
{
    @Override
    public void onInitialize()
    {
        ToughAsNails.init();
    }

    @Override
    public void onInitializeClient()
    {
        ToughAsNails.setupClient();
    }
}

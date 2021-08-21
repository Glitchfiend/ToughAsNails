/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraftforge.common.capabilities.CapabilityManager;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.thirst.IThirst;
import toughasnails.thirst.ThirstData;

public class ModCapabilities
{
    public static void init()
    {
        CapabilityManager.INSTANCE.register(IThirst.class);
        CapabilityManager.INSTANCE.register(ITemperature.class);
    }
}

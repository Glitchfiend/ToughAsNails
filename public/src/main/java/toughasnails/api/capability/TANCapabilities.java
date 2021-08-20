/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.thirst.IThirst;

public class TANCapabilities
{
    @CapabilityInject(IThirst.class)
    public static final Capability<IThirst> THIRST = null;

    @CapabilityInject(ITemperature.class)
    public static final Capability<ITemperature> TEMPERATURE = null;
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.thirst.IThirst;

public class TANCapabilities
{
    public static final Capability<IThirst> THIRST = CapabilityManager.get(new CapabilityToken<IThirst>(){});
    public static final Capability<ITemperature> TEMPERATURE = CapabilityManager.get(new CapabilityToken<ITemperature>(){});
}

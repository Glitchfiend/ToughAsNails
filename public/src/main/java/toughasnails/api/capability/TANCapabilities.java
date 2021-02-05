/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 *
 * All Rights Reserved.
 ******************************************************************************/
package toughasnails.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import toughasnails.api.thirst.IThirst;

public class TANCapabilities
{
    @CapabilityInject(IThirst.class)
    public static final Capability<IThirst> THIRST = null;
}

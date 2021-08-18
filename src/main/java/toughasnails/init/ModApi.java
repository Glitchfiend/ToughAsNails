/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.temperature.TemperatureHelperImpl;
import toughasnails.thirst.ThirstHelperImpl;

public class ModApi
{
    public static void init()
    {
        ThirstHelper.Impl.INSTANCE = new ThirstHelperImpl();
        TemperatureHelper.Impl.INSTANCE = new TemperatureHelperImpl();
    }
}

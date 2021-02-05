/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstHelperImpl;

public class ModApi
{
    public static void init()
    {
        ThirstHelper.Impl.INSTANCE = new ThirstHelperImpl();
    }
}

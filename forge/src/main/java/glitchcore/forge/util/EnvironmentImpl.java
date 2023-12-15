/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.util;

import net.minecraftforge.fml.loading.FMLEnvironment;

public final class EnvironmentImpl
{
    public static boolean isClient()
    {
        return FMLEnvironment.dist.isClient();
    }
}

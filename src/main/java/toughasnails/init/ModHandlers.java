/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;
import toughasnails.thirst.ThirstOverlayHandler;

public class ModHandlers
{
    public static void init()
    {
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(new ThirstOverlayHandler());
        }
    }
}

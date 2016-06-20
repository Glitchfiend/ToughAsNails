/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import toughasnails.config.GameplayConfigurationHandler;

public class ModConfig
{
    public static void init(File configDirectory)
    {
        GameplayConfigurationHandler.init(new File(configDirectory, "gameplay.cfg"));
        MinecraftForge.EVENT_BUS.register(new GameplayConfigurationHandler());
    }
}

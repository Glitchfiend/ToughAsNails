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
import toughasnails.config.GameplayConfig;
import toughasnails.config.SeasonsConfig;
import toughasnails.core.ToughAsNails;

public class ModConfig
{
    public static GameplayConfig gameplay;
    public static SeasonsConfig seasons;

    public static void init(File configDirectory)
    {
        gameplay = new GameplayConfig(new File(configDirectory, "gameplay.cfg"));
        seasons = new SeasonsConfig(new File(configDirectory, "seasons.cfg"));
    }
}

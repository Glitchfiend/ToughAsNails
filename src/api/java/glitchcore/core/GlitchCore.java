/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = GlitchCore.MOD_ID, version = GlitchCore.MOD_VERSION, name = GlitchCore.MOD_NAME, dependencies = "required-after:forge@[1.0.0.0,)")
public class GlitchCore
{
    public static final String MOD_NAME = "Glitch Core";
    public static final String MOD_ID = "glitchcore";
    public static final String MOD_VERSION = "@MOD_VERSION@";

    @Mod.Instance(MOD_ID)
    public static GlitchCore instance;

    public static Logger logger = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}

/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.launch;

import java.io.File;
import java.util.List;

import org.spongepowered.asm.launch.MixinTweaker;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class TANLaunchTweaker implements ITweaker
{
    private static MixinTweaker mixinTweaker;
    
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) 
    {
        mixinTweaker = new MixinTweaker();
        mixinTweaker.acceptOptions(args, gameDir, assetsDir, profile);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) 
    {
        mixinTweaker.injectIntoClassLoader(classLoader);
    }

    @Override
    public String getLaunchTarget() 
    {
        return "";
    }

    @Override
    public String[] getLaunchArguments() 
    {
        return new String[0];
    }

    
}

/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.temperature.modifier.SeasonModifier;

public class ModCompat
{
    @GameRegistry.ObjectHolder("biomesoplenty:hot_spring_water")
    public static final Block HOT_SPRING_WATER = null;

    public static void postInit()
    {
        if (Loader.isModLoaded("sereneseasons"))
        {
            TemperatureHelper.registerTemperatureModifier(new SeasonModifier("season"));
        }
    }
}
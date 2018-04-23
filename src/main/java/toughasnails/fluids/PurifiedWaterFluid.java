/*******************************************************************************
 * Copyright 2014-2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package toughasnails.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class PurifiedWaterFluid extends Fluid {
    
    public static final String name = "purified_water";
    public static final PurifiedWaterFluid instance = new PurifiedWaterFluid();

    public PurifiedWaterFluid()
    {
        super(name, new ResourceLocation("toughasnails:blocks/purified_water_still"), new ResourceLocation("toughasnails:blocks/purified_water_flowing"));
    }

}

/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.util.math.BlockPos;

public interface ITemperatureRegulator 
{
    public Temperature getRegulatedTemperature();
    public boolean isPosRegulated(BlockPos pos);
}

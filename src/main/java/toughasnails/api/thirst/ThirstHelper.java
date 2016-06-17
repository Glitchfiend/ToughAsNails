/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.thirst;

import net.minecraft.entity.player.EntityPlayer;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.IThirst;

public class ThirstHelper 
{
    public static IThirst getThirstData(EntityPlayer player)
    {
        return player.getCapability(TANCapabilities.THIRST, null);
    }
}

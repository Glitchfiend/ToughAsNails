/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public class HealthHelper 
{
    public static final UUID STARTING_HEALTH_MODIFIER_ID = UUID.fromString("050f240e-868f-4164-a67e-374084daca71");

    public static int getInactiveHearts(EntityPlayer player)
    {
        return 10 - (int)(player.getMaxHealth() / 2);
    }
}

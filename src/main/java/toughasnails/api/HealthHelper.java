/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class HealthHelper 
{
    public static final UUID STARTING_HEALTH_MODIFIER_ID = UUID.fromString("050f240e-868f-4164-a67e-374084daca71");

    public static int getActiveHearts(EntityPlayer player)
    {
        return Math.min((int)(player.getMaxHealth() / 2), 10);
    }
    
    public static int getInactiveHearts(EntityPlayer player)
    {
        return Math.max(10 - (int)(player.getMaxHealth() / 2), 0);
    }
    
    public static void addActiveHearts(EntityPlayer player, int hearts)
    {
        IAttributeInstance maxHealthInstance = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
        AttributeModifier modifier = maxHealthInstance.getModifier(HealthHelper.STARTING_HEALTH_MODIFIER_ID);
        
        if (modifier != null)
        {
            Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
            modifier = new AttributeModifier(HealthHelper.STARTING_HEALTH_MODIFIER_ID, "Starting Health Modifier", MathHelper.clamp_int((int)modifier.getAmount() + hearts * 2, -18, 0), 0);
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getAttributeUnlocalizedName(), modifier);
            player.getAttributeMap().applyAttributeModifiers(multimap);
        }
    }
}

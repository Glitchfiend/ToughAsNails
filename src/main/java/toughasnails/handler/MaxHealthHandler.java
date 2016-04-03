/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MaxHealthHandler 
{
    private static final UUID STARTING_HEALTH_MODIFIER_ID = UUID.fromString("050f240e-868f-4164-a67e-374084daca71");
    
    //TODO: If the health config option is changed and the current health is lower
    //increase it to that new default
    @SubscribeEvent
    public void onPlayerLogin(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.worldObj;

        if (!world.isRemote)
        {
            updateStartingHealthModifier(world.getDifficulty(), player, false);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        IntegratedServer integratedServer = minecraft.getIntegratedServer();
        
        if (event.phase == Phase.END && integratedServer != null)
        {
            boolean gamePaused = Minecraft.getMinecraft().getNetHandler() != null && minecraft.isGamePaused();
            
            if (!gamePaused && minecraft.theWorld != null)
            {
                WorldInfo serverWorldInfo = integratedServer.worldServers[0].getWorldInfo();
                WorldInfo localWorldInfo = minecraft.theWorld.getWorldInfo();
                
                //This is checked before the difficulty is actually changed to make the two match in IntegratedServer's tick()
                if (localWorldInfo.getDifficulty() != serverWorldInfo.getDifficulty())
                {
                    List<EntityPlayerMP> players = integratedServer.getPlayerList().getPlayerList();
                    
                    //Update the modifiers of all the connected players
                    for (EntityPlayerMP player : players)
                    {
                        updateStartingHealthModifier(localWorldInfo.getDifficulty(), player, true);
                    }
                }
            }
        }
    }
    
    private void updateStartingHealthModifier(EnumDifficulty difficulty, EntityPlayer player, boolean forceUpdate)
    {
        IAttributeInstance maxHealthInstance = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
        double difficultyHealthDecrement;

        switch (difficulty)
        {
        case EASY:
            difficultyHealthDecrement = -6.0D;
            break;

        case NORMAL:
            difficultyHealthDecrement = -10.0D;
            break;

        case HARD:
            difficultyHealthDecrement = -14.0D;
            break;

        default:
            difficultyHealthDecrement = 0.0D;
            break;
        }
        
        AttributeModifier modifier = maxHealthInstance.getModifier(STARTING_HEALTH_MODIFIER_ID);

        //If the player doesn't have a modifier for a lowered starting health, add one
        //Or alternatively, if the player already has the attribute, update it only if it is less than the current difficulty
        //When the difficulty is changed locally in the options menu, it should always change (forceUpdate)
        if (modifier == null || modifier.getAmount() < difficultyHealthDecrement || (forceUpdate && modifier.getAmount() != difficultyHealthDecrement))
        {
            Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
            modifier = new AttributeModifier(STARTING_HEALTH_MODIFIER_ID, "Starting Health Modifier", difficultyHealthDecrement, 0);
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getAttributeUnlocalizedName(), modifier);
            player.getAttributeMap().applyAttributeModifiers(multimap);
        }
    }
}

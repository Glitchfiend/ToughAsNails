/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.health;

import java.util.List;

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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.HealthHelper;
import toughasnails.config.GameplayOption;
import toughasnails.config.SyncedConfigHandler;

public class MaxHealthHandler 
{
    //TODO: If the health config option is changed and the current health is lower
    //increase it to that new default
    @SubscribeEvent
    public void onPlayerLogin(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.world;

        if (!world.isRemote)
        {
            updateStartingHealthModifier(world.getDifficulty(), player);
        }
    }
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        IAttributeInstance oldMaxHealthInstance = event.getOriginal().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
        AttributeModifier modifier = oldMaxHealthInstance.getModifier(HealthHelper.LIFEBLOOD_HEALTH_MODIFIER_ID);
        
        //Copy the lifeblood modifier from the 'old' player
        if (SyncedConfigHandler.getBooleanValue(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH) && modifier != null)
        { 
            Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getAttributeUnlocalizedName(), modifier);
            event.getEntityPlayer().getAttributeMap().applyAttributeModifiers(multimap);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        IntegratedServer integratedServer = minecraft.getIntegratedServer();
        
        if (SyncedConfigHandler.getBooleanValue(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH) && event.phase == Phase.END && integratedServer != null)
        {
            boolean gamePaused = Minecraft.getMinecraft().getConnection() != null && minecraft.isGamePaused();
            
            if (!gamePaused && minecraft.world != null)
            {
                WorldInfo serverWorldInfo = integratedServer.worldServers[0].getWorldInfo();
                WorldInfo localWorldInfo = minecraft.world.getWorldInfo();
                
                //This is checked before the difficulty is actually changed to make the two match in IntegratedServer's tick()
                if (localWorldInfo.getDifficulty() != serverWorldInfo.getDifficulty())
                {
                    List<EntityPlayerMP> players = integratedServer.getPlayerList().getPlayerList();
                    
                    //Update the modifiers of all the connected players
                    for (EntityPlayerMP player : players)
                    {
                        updateStartingHealthModifier(localWorldInfo.getDifficulty(), player);
                    }
                }
            }
        }
    }
    
    private void updateStartingHealthModifier(EnumDifficulty difficulty, EntityPlayer player)
    {
        IAttributeInstance maxHealthInstance = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
        AttributeModifier modifier = maxHealthInstance.getModifier(HealthHelper.STARTING_HEALTH_MODIFIER_ID);
        
        //Don't update if the lowered starting health config option is disabled
        if (!SyncedConfigHandler.getBooleanValue(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH))
        {
            if (modifier != null)
            {
                maxHealthInstance.removeModifier(HealthHelper.STARTING_HEALTH_MODIFIER_ID);
            }

            return;
        }
        
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
        
        double lifebloodHearts = HealthHelper.getLifebloodHearts(player) * 2;
        double overallHealthDecrement = difficultyHealthDecrement + lifebloodHearts;
        
        //Ensure that the total hearts is never above 20 when the difficulty is changed
        if (overallHealthDecrement > 0.0D)
        {
            difficultyHealthDecrement -= overallHealthDecrement;
        }

        //If the player doesn't have a modifier for a lowered starting health, add one
        //Or alternatively, if the player already has the attribute, update it only if it is less than the current difficulty
        //When the difficulty is changed locally in the options menu, it should always change (forceUpdate)
        if (modifier == null || modifier.getAmount() != difficultyHealthDecrement)
        {
            Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
            modifier = new AttributeModifier(HealthHelper.STARTING_HEALTH_MODIFIER_ID, "Starting Health Modifier", difficultyHealthDecrement, 0);
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getAttributeUnlocalizedName(), modifier);
            player.getAttributeMap().applyAttributeModifiers(multimap);
            
            if (player.getHealth() > player.getMaxHealth())
            {
                player.setHealth(player.getMaxHealth());
            }
        }
    }
}

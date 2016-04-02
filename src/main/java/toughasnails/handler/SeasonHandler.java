/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import toughasnails.api.season.ISeasonData;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.network.message.MessageSyncSeasonCycle;
import toughasnails.season.SeasonSavedData;
import toughasnails.season.SeasonTime;

public class SeasonHandler 
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.phase == TickEvent.Phase.END && !world.isRemote && world.provider.getDimension() == 0)
        {
            SeasonSavedData savedData = getSeasonSavedData(world);

            if (savedData.seasonCycleTicks++ > SeasonTime.TOTAL_CYCLE_TICKS)
            {
                savedData.seasonCycleTicks = 0;
            }
            
            if (savedData.seasonCycleTicks % 20 == 0)
            {
                sendSeasonUpdate(world);
            }

            savedData.markDirty();
        }
    }
    
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.worldObj;
        
        sendSeasonUpdate(world);
    }

    private SubSeason lastSeason = null;
    public static int clientSeasonCycleTicks = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) 
    {
        int dimension = Minecraft.getMinecraft().thePlayer.dimension;

        if (event.phase == TickEvent.Phase.END && dimension == 0) 
        {
            //Keep ticking as we're synchronized with the server only every second
            if (clientSeasonCycleTicks++ > SeasonTime.TOTAL_CYCLE_TICKS)
            {
                clientSeasonCycleTicks = 0;
            }
            
            SeasonTime calendar = new SeasonTime(clientSeasonCycleTicks);
            
            if (calendar.getSubSeason() != lastSeason)
            {
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
                lastSeason = calendar.getSubSeason();
            }
        }
    }
    
    public static void sendSeasonUpdate(World world)
    {
        if (!world.isRemote)
        {
            SeasonSavedData savedData = getSeasonSavedData(world);
            PacketHandler.instance.sendToAll(new MessageSyncSeasonCycle(savedData.seasonCycleTicks));  
        }
    }
    
    public static SeasonSavedData getSeasonSavedData(World world)
    {
        MapStorage mapStorage = world.getPerWorldStorage();
        SeasonSavedData savedData = (SeasonSavedData)mapStorage.loadData(SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

        //If the saved data file hasn't been created before, create it
        if (savedData == null)
        {
            savedData = new SeasonSavedData(SeasonSavedData.DATA_IDENTIFIER);
            mapStorage.setData(SeasonSavedData.DATA_IDENTIFIER, savedData);
            savedData.markDirty(); //Mark for saving
        }
        
        return savedData;
    }
    
    //
    // Used to implement getSeasonData in the API
    //
    
    public static ISeasonData getServerSeasonData(World world)
    {
        SeasonSavedData savedData = getSeasonSavedData(world);
        return new SeasonTime(savedData.seasonCycleTicks);
    }
    
    public static ISeasonData getClientSeasonData()
    {
        return new SeasonTime(clientSeasonCycleTicks);
    }
}

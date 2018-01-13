/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import glitchcore.world.WorldHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.ISeasonData;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.config.SeasonsOption;
import toughasnails.handler.PacketHandler;
import toughasnails.network.message.MessageSyncSeasonCycle;
import toughasnails.season.SeasonSavedData;
import toughasnails.season.SeasonTime;

public class SeasonHandler implements SeasonHelper.ISeasonDataProvider
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.phase == TickEvent.Phase.END && !world.isRemote && world.provider.getDimension() == 0 && SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS))
        {
            SeasonSavedData savedData = getSeasonSavedData(world);

            if (savedData.seasonCycleTicks++ > SeasonTime.ZERO.getCycleDuration())
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
        World world = player.world;
        
        sendSeasonUpdate(world);
    }

    private SubSeason lastSeason = null;
    public static int clientSeasonCycleTicks = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) 
    {
        //Only do this when in the world
        if (Minecraft.getMinecraft().player == null) return;
        
        int dimension = Minecraft.getMinecraft().player.dimension;

        if (event.phase == TickEvent.Phase.END && dimension == 0 && SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS))
        {
            //Keep ticking as we're synchronized with the server only every second
            if (clientSeasonCycleTicks++ > SeasonTime.ZERO.getCycleDuration())
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
        if (!world.isRemote && SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS))
        {
            SeasonSavedData savedData = getSeasonSavedData(world);
            PacketHandler.instance.sendToAll(new MessageSyncSeasonCycle(savedData.seasonCycleTicks));  
        }
    }
    
    public static SeasonSavedData getSeasonSavedData(World world)
    {
        MapStorage mapStorage = world.getPerWorldStorage();
        SeasonSavedData savedData = WorldHelper.getOrLoadSavedData(world, SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

        //If the saved data file hasn't been created before, create it
        if (savedData == null)
        {
            savedData = new SeasonSavedData(SeasonSavedData.DATA_IDENTIFIER);
            WorldHelper.setData(world, SeasonSavedData.DATA_IDENTIFIER, savedData);
            savedData.markDirty(); //Mark for saving
        }
        
        return savedData;
    }
    
    //
    // Used to implement getSeasonData in the API
    //
    
    public ISeasonData getServerSeasonData(World world)
    {
        SeasonSavedData savedData = getSeasonSavedData(world);
        return new SeasonTime(savedData.seasonCycleTicks);
    }
    
    public ISeasonData getClientSeasonData()
    {
        return new SeasonTime(clientSeasonCycleTicks);
    }
}

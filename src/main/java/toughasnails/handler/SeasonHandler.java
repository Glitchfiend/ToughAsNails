/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler;

import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.network.message.MessageSyncSeasonCycle;
import toughasnails.season.SeasonSavedData;
import toughasnails.season.SeasonTime;
import toughasnails.util.SeasonColourUtil;

public class SeasonHandler 
{
    public static final int MIDNIGHT_TIME = 18000;
    
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.phase == TickEvent.Phase.END && !world.isRemote && world.provider.getDimension() == 0)
        {
            SeasonSavedData savedData = getSeasonData(world);

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
        WorldClient world = Minecraft.getMinecraft().theWorld;

        if (event.phase == TickEvent.Phase.END && world != null && world.provider.getDimension() == 0) 
        {
            //Keep ticking as we're synchronized with the server only every second
            if (clientSeasonCycleTicks++ > SeasonTime.TOTAL_CYCLE_TICKS)
            {
                clientSeasonCycleTicks = 0;
            }
            
            SeasonTime calendar = new SeasonTime(clientSeasonCycleTicks);
            
            //DEBUG
            if (clientSeasonCycleTicks % 100 == 0)
            {
                System.out.println(calendar.getSubSeason());
            }
            
            if (world != null && calendar.getSubSeason() != lastSeason)
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
            SeasonSavedData savedData = getSeasonData(world);
            PacketHandler.instance.sendToAll(new MessageSyncSeasonCycle(savedData.seasonCycleTicks));  
        }
    }
    
    public static SeasonSavedData getSeasonData(World world)
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
}

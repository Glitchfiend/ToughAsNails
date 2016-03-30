/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler;

import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import toughasnails.api.season.Season;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.season.Calendar;
import toughasnails.season.SeasonSavedData;

public class SeasonHandler 
{
    public static final int MIDNIGHT_TIME = 18000;

    /**Stores the saved data for each dimension**/
    private HashMap<Integer, SeasonSavedData> seasonsSavedData = Maps.newHashMap();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        World world = event.getWorld();
        int dimensionId = world.provider.getDimension();

        //For now, we only want seasons in the overworld
        if (dimensionId != 0) return;

        MapStorage mapStorage = world.getPerWorldStorage();
        SeasonSavedData savedData = (SeasonSavedData)mapStorage.loadData(SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

        //If the saved data file hasn't been created before, create it
        if (savedData == null)
        {
            savedData = new SeasonSavedData(SeasonSavedData.DATA_IDENTIFIER);
            mapStorage.setData(SeasonSavedData.DATA_IDENTIFIER, savedData);
            savedData.markDirty(); //Mark for saving
        }

        //Cache saved data object
        this.seasonsSavedData.put(dimensionId, savedData);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.phase == TickEvent.Phase.END && world.provider.getDimension() == 0)
        {
            SeasonSavedData savedData = this.seasonsSavedData.get(world.provider.getDimension());

            Calendar calendar = new Calendar(savedData);

            //System.out.println(savedData.seasonCycleTicks);
            System.out.println(calendar.getSubSeason());

            if (savedData.seasonCycleTicks++ > Calendar.TOTAL_CYCLE_TICKS)
            {
                savedData.seasonCycleTicks = 0;
            }
            savedData.markDirty();
        }
    }

    private SubSeason lastSeason = null;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) 
    {
        WorldClient world = Minecraft.getMinecraft().theWorld;

        //Update colours at midnight if suitable
        if (event.phase == TickEvent.Phase.END && world != null && world.provider.getDimension() == 0) 
        {
            SeasonSavedData savedData = this.seasonsSavedData.get(world.provider.getDimension());

            Calendar calendar = new Calendar(savedData);

            if (world != null && calendar.getSubSeason() != lastSeason)
            {
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
                lastSeason = calendar.getSubSeason();
            }
        }
    }

    @SubscribeEvent
    public void onGrassColourChange(BiomeEvent.GetGrassColor event)
    {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        SeasonSavedData savedData = this.seasonsSavedData.get(world.provider.getDimension());

        Calendar calendar = new Calendar(savedData);

        switch (calendar.getSubSeason())
        {
        case EARLY_SPRING:
            event.setNewColor(0xFF0000);
            break;
        case MID_SPRING:
            event.setNewColor(0xFF6A00);
            break;
        case LATE_SPRING:
            event.setNewColor(0xFFD800);
            break;
        case EARLY_SUMMER:
            event.setNewColor(0xB6FF00);
            break;
        case MID_SUMMER:
            event.setNewColor(0x00FF21);
            break;
        case LATE_SUMMER:
            event.setNewColor(0x00FF90);
            break;
        case EARLY_AUTUMN:
            event.setNewColor(0x00FFFF);
            break;
        case MID_AUTUMN:
            event.setNewColor(0x0094FF);
            break;
        case LATE_AUTUMN:
            event.setNewColor(0x0026FF);
            break;
        case EARLY_WINTER:
            event.setNewColor(0x4800FF);
            break;
        case MID_WINTER:
            event.setNewColor(0xB200FF);
            break;
        case LATE_WINTER:
            event.setNewColor(0xFF00DC);
            break;
        }
    }
}

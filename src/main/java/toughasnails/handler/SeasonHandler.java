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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import toughasnails.season.SeasonColors;
import toughasnails.season.SeasonSavedData;

public class SeasonHandler 
{
    public static final int MIDNIGHT_TIME = 18000;

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
    }
    
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.phase == TickEvent.Phase.END && world.provider.getDimension() == 0)
        {
            MapStorage mapStorage = world.getPerWorldStorage();
            SeasonSavedData savedData = (SeasonSavedData)mapStorage.loadData(SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

            Calendar calendar = new Calendar(savedData);

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
            MapStorage mapStorage = world.getPerWorldStorage();
            SeasonSavedData savedData = (SeasonSavedData)mapStorage.loadData(SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

            Calendar calendar = new Calendar(savedData);

            if (world != null && calendar.getSubSeason() != lastSeason)
            {
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
                lastSeason = calendar.getSubSeason();
            }
        }
    }

    //TODO: We need to use actual positions for these, not just the origin
    
    @SubscribeEvent
    public void onGrassColourChange(BiomeEvent.GetGrassColor event)
    {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        MapStorage mapStorage = world.getPerWorldStorage();
        SeasonSavedData savedData = (SeasonSavedData)mapStorage.loadData(SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

        Calendar calendar = new Calendar(savedData);

        double temperature = (double)MathHelper.clamp_float(event.getBiome().getFloatTemperature(BlockPos.ORIGIN), 0.0F, 1.0F);
        double rainfall = (double)MathHelper.clamp_float(event.getBiome().getRainfall(), 0.0F, 1.0F);
        event.setNewColor(SeasonColors.getGrassColorForSeason(calendar.getSubSeason(), temperature, rainfall));
    }
    
    @SubscribeEvent
    public void onGrassColourChange(BiomeEvent.GetFoliageColor event)
    {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        MapStorage mapStorage = world.getPerWorldStorage();
        SeasonSavedData savedData = (SeasonSavedData)mapStorage.loadData(SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

        Calendar calendar = new Calendar(savedData);

        double temperature = (double)MathHelper.clamp_float(event.getBiome().getFloatTemperature(BlockPos.ORIGIN), 0.0F, 1.0F);
        double rainfall = (double)MathHelper.clamp_float(event.getBiome().getRainfall(), 0.0F, 1.0F);
        event.setNewColor(SeasonColors.getFoliageColorForSeason(calendar.getSubSeason(), temperature, rainfall));
    }
}

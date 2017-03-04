/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.config.GameplayOption;

public class StopSpawnHandler 
{
    //Animals shouldn't spawn during winter
    @SubscribeEvent
    public void onCheckEntitySpawn(LivingSpawnEvent.CheckSpawn event)
    {
        Season season = SeasonHelper.getSeasonData(event.getWorld()).getSubSeason().getSeason();
        
        if (season == Season.WINTER && event.getEntity() instanceof EntityAnimal && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS))
        {
            event.setResult(Result.DENY);
        }
    }
    
    @SubscribeEvent
    public void onChunkPopulate(PopulateChunkEvent.Populate event)
    {
        World world = event.getWorld();
        Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
        
        //Prevent animals from spawning in new chunks during the winter
        if (event.getType() == EventType.ANIMALS && season == Season.WINTER && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS))
        {
            event.setResult(Result.DENY);
        }
    }
}

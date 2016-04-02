/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;

public class StopSpawnHandler 
{
    //Animals shouldn't spawn during winter
    @SubscribeEvent
    public void onEntitySpawn(LivingSpawnEvent.CheckSpawn event)
    {
        Season season = SeasonHelper.getSeasonData(event.getWorld()).getSubSeason().getSeason();
        
        if (season == Season.WINTER && event.getEntity() instanceof EntityAnimal)
        {
            event.setResult(Result.DENY);
        }
    }
}

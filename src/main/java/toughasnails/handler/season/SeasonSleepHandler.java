/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.config.GameplayOption;
import toughasnails.season.SeasonSavedData;

public class SeasonSleepHandler 
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == Phase.START && event.side == Side.SERVER && SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS))
        {
            WorldServer world = (WorldServer)event.world;

            //Called before all players are awoken for the next day
            if (world.areAllPlayersAsleep())
            {
                SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
                Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
                
                long timeDiff = 24000L - ((world.getWorldInfo().getWorldTime() + 24000L) % 24000L);
                seasonData.seasonCycleTicks += timeDiff;
/*                if( season == Season.WINTER && world.isRaining() )
                	seasonData.shiftSnowWindow( (int)timeDiff, true );
                else if( season != Season.WINTER )
                	seasonData.shiftSnowWindow( (int)timeDiff, false ); */
                seasonData.updateJournal(world, season);
                
                seasonData.markDirty();
                SeasonHandler.sendSeasonUpdate(world);
            }
        }
    }
}

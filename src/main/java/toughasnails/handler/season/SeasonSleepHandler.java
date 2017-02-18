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
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.config.GameplayOption;
import toughasnails.season.SeasonSavedData;

public class SeasonSleepHandler 
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == Phase.START && event.side == Side.SERVER && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS))
        {
            WorldServer world = (WorldServer)event.world;
            
            //Called before all players are awoken for the next day
            if (world.areAllPlayersAsleep())
            {
                SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
                /*
                 * 23460L is the tick value for Dawn (When a player wakes from bed)
                 * Caveat: timeDiff may be negative if a mod edits when a player is allowed to sleep.
                 */
                long timeDiff = 23460L - (world.getWorldInfo().getWorldTime());
                if (timeDiff <= 0) {
                	timeDiff += 24000L; //This covers our caveat
                }
                seasonData.seasonCycleTicks += timeDiff;
                seasonData.markDirty();
                SeasonHandler.sendSeasonUpdate(world);
            }
        }
    }
}

/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;

public class WeatherFrequencyHandler 
{
    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
        if (event.phase == Phase.START && event.side == Side.SERVER)
        {
            World world = event.world;
            Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
            
            //During winter, the absolute maximum delay between rain/thunder should be 30 mins, unlike
            //the standard 160 minutes. We only wish to modify the time between rain/thunder occurring, not
            //their duration.
            if (season == Season.WINTER)
            {
                if (!world.isThundering() && world.getWorldInfo().getThunderTime() <= 36000)
                {
                    world.getWorldInfo().setThunderTime(world.rand.nextInt(24000) + 12000);
                }

                if (!world.isRaining() && world.getWorldInfo().getRainTime() <= 36000)
                {
                    world.getWorldInfo().setRainTime(world.rand.nextInt(24000) + 12000);
                }
            }
        }
    }
}

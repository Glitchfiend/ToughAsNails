/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.IHibernatingCrop;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;

public class SeasonCropHandler {
    @SubscribeEvent
    public void onCropGrowPre(BlockEvent.CropGrowEvent.Pre event) {
        Block block = event.getState().getBlock();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
        if (season == Season.WINTER &&
                (block instanceof IHibernatingCrop && ((IHibernatingCrop)block).shouldHibernate()) &&
                !TemperatureHelper.isPosClimatisedForTemp(world, pos, new Temperature(1)) &&
                SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS)
                ) {
            event.setResult(Event.Result.DENY);
        }
    }
}

/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.season.SeasonASMHelper;

public class ProviderIceHandler 
{
    /**Handle our own ice generation to ignore winter*/
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPopulateChunkEvent(PopulateChunkEvent.Populate event)
    {
        World world = event.getWorld();
        BlockPos pos = new BlockPos(event.getChunkX() * 16, 0, event.getChunkZ() * 16).add(8, 0, 8);
        
        if (event.getType() == EventType.ICE)
        {
            for (int k2 = 0; k2 < 16; ++k2)
            {
                for (int j3 = 0; j3 < 16; ++j3)
                {
                    BlockPos blockpos1 = world.getPrecipitationHeight(pos.add(k2, 0, j3));
                    BlockPos blockpos2 = blockpos1.down();

                    if (SeasonASMHelper.canBlockFreezeInSeason(world, blockpos2, false, null))
                    {
                    	world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
                    }

                    if (SeasonASMHelper.canSnowAtInSeason(world, blockpos1, true, null))
                    {
                    	world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
                    }
                }
            }
            
            event.setResult(Result.DENY);
        }
    }
}

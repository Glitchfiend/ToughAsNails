/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import java.util.Iterator;

import net.minecraft.block.BlockIce;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;

public class RandomUpdateHandler 
{
    //Randomly melt ice and snow when it isn't winter
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == Phase.END && !event.world.isRemote)
        {
            WorldServer world = (WorldServer)event.world;
            Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
            
            //Only melt when it isn't winter and it isn't raining
            if (season != Season.WINTER && !world.isRaining())
            {
                for (Iterator<Chunk> iterator = world.getPersistentChunkIterable(world.getPlayerChunkManager().getChunkIterator()); iterator.hasNext();)
                {
                    Chunk chunk = (Chunk)iterator.next();
                    int x = chunk.xPosition * 16;
                    int z = chunk.zPosition * 16;

                    if (world.rand.nextInt(season == Season.SUMMER ? 8 : 16) == 0)
                    {
                        world.updateLCG = world.updateLCG * 3 + 1013904223;
                        int randOffset = world.updateLCG >> 2;
                        BlockPos topPos = world.getPrecipitationHeight(new BlockPos(x + (randOffset & 15), 0, z + (randOffset >> 8 & 15)));
                        BlockPos groundPos = topPos.down();

                        if (world.getBlockState(groundPos).getBlock() == Blocks.ice && !SeasonHelper.canSnowAtTempInSeason(season, world.getBiomeGenForCoords(groundPos).getFloatTemperature(groundPos)))
                        {
                            ((BlockIce)Blocks.ice).func_185679_b(world, groundPos);
                        }

                        if (world.getBlockState(topPos).getBlock() == Blocks.snow_layer && !SeasonHelper.canSnowAtTempInSeason(season, world.getBiomeGenForCoords(topPos).getFloatTemperature(topPos)))
                        {
                            world.setBlockToAir(topPos);
                        }
                    }
                }
            }
        }
    }
}

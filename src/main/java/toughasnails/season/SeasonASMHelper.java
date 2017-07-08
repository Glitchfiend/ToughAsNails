/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.TANBlocks;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.IDecayableCrop;
import toughasnails.api.season.IHibernatingCrop;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.handler.season.SeasonHandler;

public class SeasonASMHelper
{
    ///////////////////
    // World methods //
    ///////////////////
    
    public static boolean canSnowAtInSeason(World world, BlockPos pos, boolean checkLight, Season season)
    {
        Biome biome = world.getBiomeGenForCoords(pos);
        float temperature = biome.getFloatTemperature(pos);
        
        //If we're in winter, the temperature can be anything equal to or below 0.7
        if (!SeasonHelper.canSnowAtTempInSeason(season, temperature))
        {
            return false;
        }
        else if (biome == Biomes.RIVER || biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN)
        {
            return false;
        }
        else if (checkLight)
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState state = world.getBlockState(pos);

                if (state.getBlock().isAir(state, world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))
                {
                    return true;
                }
            }

            return false;
        }
        
        return true;
    }
    
    public static boolean canBlockFreezeInSeason(World world, BlockPos pos, boolean noWaterAdj, Season season)
    {
        Biome Biome = world.getBiomeGenForCoords(pos);
        float temperature = Biome.getFloatTemperature(pos);
        
        //If we're in winter, the temperature can be anything equal to or below 0.7
        if (!SeasonHelper.canSnowAtTempInSeason(season, temperature))
        {
            return false;
        }
        else if (Biome == Biomes.RIVER || Biome == Biomes.OCEAN || Biome == Biomes.DEEP_OCEAN)
        {
            return false;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate = world.getBlockState(pos);
                Block block = iblockstate.getBlock();

                if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                {
                    if (!noWaterAdj)
                    {
                        return true;
                    }

                    boolean flag = world.isWater(pos.west()) && world.isWater(pos.east()) && world.isWater(pos.north()) && world.isWater(pos.south());

                    if (!flag)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }
    
    public static boolean isRainingAtInSeason(World world, BlockPos pos, Season season)
    {
        Biome biome = world.getBiomeGenForCoords(pos);
        return biome.getEnableSnow() && season != Season.WINTER ? false : (world.canSnowAt(pos, false) ? false : biome.canRain());
    }
    
    ///////////////////
    // Biome methods //
    ///////////////////
    
    public static float getFloatTemperature(Biome biome, BlockPos pos)
    {
        Season season = new SeasonTime(SeasonHandler.clientSeasonCycleTicks).getSubSeason().getSeason();
        
        if (biome.getTemperature() <= 0.7F && season == Season.WINTER && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS))
        {
            return 0.0F;
        }
        else
        {
            return biome.getFloatTemperature(pos);
        }
    }
    
    ////////////////////////
    // BlockCrops methods //
    ////////////////////////
    
    public static void onUpdateTick(Block block, World world, BlockPos pos)
    {
        Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
        
        if (season == Season.WINTER && block instanceof IDecayableCrop && !TemperatureHelper.isPosClimatisedForTemp(world, pos, new Temperature(1)) && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS))
        {
            world.setBlockState(pos, TANBlocks.dead_crops.getDefaultState());
        }
    }
    
    public static boolean shouldHibernate(Block block, World world, BlockPos pos)
    {
        Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
        
        return (season == Season.WINTER && block instanceof IHibernatingCrop && !TemperatureHelper.isPosClimatisedForTemp(world, pos, new Temperature(1)) && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS));
    }
}

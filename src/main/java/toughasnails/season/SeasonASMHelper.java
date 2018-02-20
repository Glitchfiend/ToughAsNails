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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.TANBlocks;
import toughasnails.api.season.IDecayableCrop;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.config.GameplayOption;
import toughasnails.handler.season.SeasonHandler;
import toughasnails.init.ModConfig;

public class SeasonASMHelper
{
    ///////////////////
    // World methods //
    ///////////////////
    
    public static boolean canSnowAtInSeason(World world, BlockPos pos, boolean checkLight, Season season)
    {
        Biome biome = world.getBiome(pos);
        float temperature = biome.getTemperature(pos);
        
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
        Biome Biome = world.getBiome(pos);
        float temperature = Biome.getTemperature(pos);
        
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
        Biome biome = world.getBiome(pos);
        return biome.getEnableSnow() && season != Season.WINTER ? false : (world.canSnowAt(pos, false) ? false : biome.canRain());
    }
    
    ///////////////////
    // Biome methods //
    ///////////////////
    
    public static float getFloatTemperature(Biome biome, BlockPos pos)
    {
        Season season = new SeasonTime(SeasonHandler.clientSeasonCycleTicks).getSubSeason().getSeason();
        
        if (biome.getDefaultTemperature() <= 0.8F && season == Season.WINTER && SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS))
        {
            return 0.0F;
        }
        else
        {
            return biome.getTemperature(pos);
        }
    }
    
    ////////////////////////
    // BlockCrops methods //
    ////////////////////////
    
    public static void onRandomTick(Block block, World world, BlockPos pos)
    {
        if (!(block instanceof IDecayableCrop) || !((IDecayableCrop)block).shouldDecay()) return;

        Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
        
        if (season == Season.WINTER &&
                (block instanceof IDecayableCrop && ((IDecayableCrop)block).shouldDecay()) &&
                !TemperatureHelper.isPosClimatisedForTemp(world, pos, new Temperature(1)) && 
                SyncedConfig.getBooleanValue(SeasonsOption.ENABLE_SEASONS) && ModConfig.seasons.winterCropDeath
                )
        {
            world.setBlockState(pos, TANBlocks.dead_crops.getDefaultState());
        }
    }
    
    // Calculates the sun duration in a day according to the current time of year (season)
    public static long calculateSunDuration(float latitude)
    {
    	long minSunDuration = 2000; // TODO: Should depend on the given latitude (or not)
    	long maxSunDuration = SeasonTime.ZERO.getDayDuration() - minSunDuration;
    	long sunDuration = 0;
    	long currentTime = SeasonHandler.clientSeasonCycleTicks;
    	float phaseShift = (float) SeasonTime.ZERO.getSeasonDuration() * 2.5F;
    	
    	// The sun duration is maximised on the summer solstice and minimised on the winter solstice (for now it's a northern point of view)
    	sunDuration = (long) ((MathHelper.cos((float) ((currentTime + phaseShift) * Math.PI / ((float) SeasonTime.ZERO.getCycleDuration() / 2.0F))) + 1.0F) / 2.0F * (float) (maxSunDuration - minSunDuration) + minSunDuration);
    	//System.out.println("time:" + currentTime + "  sun duration:" + sunDuration);
    	return sunDuration;
    }
    
    // Calculates the angle of the sun and the moon in the sky relative to a specified time (usually worldTime)
    public static float calculateCelestialAngle(long worldTime, float partialTicks)
    {
    	/* Values to return:
    	 * midday: 0 (or 1 excluded)
    	 * sunset: 0.25
    	 * midnight: 0.5
    	 * sunrise: 0.75
    	 * etc.
    	 */
        
        // Adapt celestial angle to chosen day-night duration centered on midday and midnight
    	// TODO: Vanilla code: WTF are "partial ticks"? What's the point of cos(f*pi)?
    	// TODO: Smoother acceleration between celestial phases (on sunset and on sunrise)
    	
    	float latitude = 0;
    	long sunDuration = calculateSunDuration(latitude);
    	long zenithTime = 6000;
    	float angle = 0;
    	
    	// Lock the sun at its zenith
    	if (sunDuration == 24000)
    		return 0.0F;
    	
    	// Lock the moon at its zenith
    	if (sunDuration == 0)
    		return 0.5F;
    	
    	// Normalisation: makes the day phase contiguous so that it's easier to process the different celestial phases
    	long dayTime = (worldTime + 6000) % 24000;
    	zenithTime += 6000;
    	
    	// Phase 1: daytime
        if (dayTime >= zenithTime - sunDuration / 2 && dayTime <= zenithTime + sunDuration / 2)
        	angle =  (float)(dayTime) / (float)(sunDuration) / 2.0F + 1.0F - 6000F / (float) sunDuration;
        
        // Phase 2: from sunset to midnight
        else if (dayTime > zenithTime + sunDuration / 2)
        	angle = 0.25F / (12000F - sunDuration / 2) * (float)(dayTime) + 1.5F - 6000F / (12000F - sunDuration / 2);
        
        // Phase 3: from midnight to sunrise (should be almost the same as phase 2)
        else if (dayTime < zenithTime - sunDuration / 2)
        	angle = 0.25F / (12000F - sunDuration / 2) * (float)(dayTime + 24000) + 1.5F - 6000F / (12000F - sunDuration / 2);
        
        if (angle > 1.0F)
    		--angle;
        
    	//System.out.println("time=" + dayTime + " (real=" + worldTime + ") angle=" + angle);
        return angle;
    }
}

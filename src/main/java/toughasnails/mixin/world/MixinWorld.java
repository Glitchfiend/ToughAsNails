/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.mixin.world;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;

@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess
{
    @Shadow
    public abstract int getLightFor(EnumSkyBlock type, BlockPos pos);

    @Shadow
    public abstract boolean isWater(BlockPos pos);
    
    //Replace these methods in world to account for winter
    @Overwrite
    public boolean canSnowAtBody(BlockPos pos, boolean checkLight)
    {
        BiomeGenBase biome = this.getBiomeGenForCoords(pos);
        float temperature = biome.getFloatTemperature(pos);
        Season season = SeasonHelper.getSeasonData((World)(Object)this).getSubSeason().getSeason();
        
        //If we're in winter, the temperature can be anything equal to or below 0.7
        if (temperature > 0.15F && !(season == Season.WINTER && temperature <= 0.7F))
        {
            return false;
        }
        else if (checkLight)
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState state = this.getBlockState(pos);

                if (state.getBlock().isAir(state, this, pos) && Blocks.snow_layer.canPlaceBlockAt((World)(Object)this, pos))
                {
                    return true;
                }
            }

            return false;
        }
        
        return true;
    }
    
    @Overwrite
    public boolean canBlockFreezeBody(BlockPos pos, boolean noWaterAdj)
    {
        BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
        float temperature = biomegenbase.getFloatTemperature(pos);
        Season season = SeasonHelper.getSeasonData((World)(Object)this).getSubSeason().getSeason();
        
        //If we're in winter, the temperature can be anything equal to or below 0.7
        if (temperature > 0.15F && !(season == Season.WINTER && temperature <= 0.7F))
        {
            return false;
        }
        else
        {
            if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
            {
                IBlockState iblockstate = this.getBlockState(pos);
                Block block = iblockstate.getBlock();

                if ((block == Blocks.water || block == Blocks.flowing_water) && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                {
                    if (!noWaterAdj)
                    {
                        return true;
                    }

                    boolean flag = this.isWater(pos.west()) && this.isWater(pos.east()) && this.isWater(pos.north()) && this.isWater(pos.south());

                    if (!flag)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}

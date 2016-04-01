/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.mixin.block;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.api.TANBlocks;
import toughasnails.api.season.IDecayableCrop;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;

@Mixin(BlockCrops.class)
public abstract class MixinBlockCrops extends BlockBush implements IGrowable
{
    @Inject(method = "updateTick", at = @At("RETURN"))
    public void onUpdateTick(World world, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci) 
    {
        Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
        
        if (season == Season.WINTER && (this.delegate.getResourceName().getResourceDomain().equals("minecraft") || this instanceof IDecayableCrop))
        {
            world.setBlockState(pos, TANBlocks.dead_crops.getDefaultState());
        }
    }
}

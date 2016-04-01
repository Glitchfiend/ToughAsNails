/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.mixin.client.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.api.season.Season;
import toughasnails.handler.SeasonHandler;
import toughasnails.season.SeasonTime;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IResourceManagerReloadListener
{
    //
    // Redirect calls to getFloatTemperature here on the client only.
    //
    
    //Render snow instead of rain
    @Redirect(method = "renderRainSnow(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeGenBase;getFloatTemperature(Lnet/minecraft/util/math/BlockPos;)F"))
    private float onGetFloatTemperature1(BiomeGenBase this$0, BlockPos pos) 
    {
        Season season = new SeasonTime(SeasonHandler.clientSeasonCycleTicks).getSubSeason().getSeason();
        
        if (season == Season.WINTER)
        {
            return 0.0F;
        }
        else
        {
            return this$0.getFloatTemperature(pos);
        }
    }
    
    //Prevent adding rain particles
    @Redirect(method = "addRainParticles()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeGenBase;getFloatTemperature(Lnet/minecraft/util/math/BlockPos;)F"))
    private float onGetFloatTemperature2(BiomeGenBase this$0, BlockPos pos) 
    {
        Season season = new SeasonTime(SeasonHandler.clientSeasonCycleTicks).getSubSeason().getSeason();
        
        if (season == Season.WINTER)
        {
            return 0.0F;
        }
        else
        {
            return this$0.getFloatTemperature(pos);
        }
    }
    
}

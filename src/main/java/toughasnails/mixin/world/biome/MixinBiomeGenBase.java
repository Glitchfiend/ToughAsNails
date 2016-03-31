/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.mixin.world.biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.handler.SeasonHandler;
import toughasnails.season.Calendar;
import toughasnails.season.SeasonColors;

@Mixin(BiomeGenBase.class)
public abstract class MixinBiomeGenBase 
{
    @Shadow
    abstract float getFloatTemperature(BlockPos pos);
    
    @Shadow
    abstract float getRainfall();
    
    @Inject(method = "getGrassColorAtPos(Lnet/minecraft/util/math/BlockPos;)I", at = @At("RETURN"), cancellable = true)
    public void onGetGrassColorAtPos(BlockPos pos, CallbackInfoReturnable<Integer> cir) 
    {
        Calendar calendar = new Calendar(SeasonHandler.clientSeasonCycleTicks);
        
        double temperature = (double)MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0F, 1.0F);
        double rainfall = (double)MathHelper.clamp_float(this.getRainfall(), 0.0F, 1.0F);
        cir.setReturnValue(SeasonColors.getGrassColorForSeason(calendar.getSubSeason(), temperature, rainfall));
    }
    
    @Inject(method = "getFoliageColorAtPos(Lnet/minecraft/util/math/BlockPos;)I", at = @At("RETURN"), cancellable = true)
    public void onGetFoliageColorAtPos(BlockPos pos, CallbackInfoReturnable<Integer> cir) 
    {
        Calendar calendar = new Calendar(SeasonHandler.clientSeasonCycleTicks);

        double temperature = (double)MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0F, 1.0F);
        double rainfall = (double)MathHelper.clamp_float(this.getRainfall(), 0.0F, 1.0F);
        cir.setReturnValue(SeasonColors.getFoliageColorForSeason(calendar.getSubSeason(), temperature, rainfall));
    }
}

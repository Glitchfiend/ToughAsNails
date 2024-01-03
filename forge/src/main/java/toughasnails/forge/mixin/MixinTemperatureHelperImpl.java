/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import toughasnails.forge.init.ModCompatibility;
import toughasnails.temperature.TemperatureHelperImpl;

@Mixin(value = TemperatureHelperImpl.class, remap = false)
public abstract class MixinTemperatureHelperImpl
{
    @Overwrite
    private static boolean coldEnoughToSnow(Level level, Holder<Biome> biome, BlockPos pos)
    {
        if (ModList.get().isLoaded("sereneseasons"))
        {
            return ModCompatibility.coldEnoughToSnowSeasonal(level, biome, pos);
        }
        else
        {
            return biome.value().coldEnoughToSnow(pos);
        }
    }
}

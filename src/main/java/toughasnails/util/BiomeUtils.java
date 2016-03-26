package toughasnails.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeUtils 
{
    /**Get biome temperature on a scale of 0 to 1, 0 freezing and 1 boiling hot**/
    public static float getBiomeTempNorm(BiomeGenBase biome)
    {
        return MathHelper.clamp_float(biome.getTemperature(), 0.0F, 1.35F) / 1.35F;
    }
    
    /**Get the biome temperature's level of extremity from 0 to 1, 0 least extreme and 1 most extreme*/
    public static float getBiomeTempExtremity(BiomeGenBase biome)
    {
        return Math.abs(getBiomeTempNorm(biome) * 2.0F - 1.0F);
    }
}

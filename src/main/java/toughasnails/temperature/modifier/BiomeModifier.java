package toughasnails.temperature.modifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;
import toughasnails.util.BiomeUtils;
import toughasnails.util.TerrainUtils;

public class BiomeModifier extends TemperatureModifier
{
    public BiomeModifier(String id)
    {
        super(id);
    }

    @Override
    public Temperature applyEnvironmentModifiers(World world, BlockPos pos, Temperature initialTemperature, IModifierMonitor monitor)
    {
        Biome biome = world.getBiome(pos);
        Biome biomeNorth = world.getBiome(pos.add(0, 0, -10));
        Biome biomeSouth = world.getBiome(pos.add(0, 0, 10));
        Biome biomeEast = world.getBiome(pos.add(10, 0, 0));
        Biome biomeWest = world.getBiome(pos.add(-10, 0, 0));
        
        float biomeTemp = ((BiomeUtils.getBiomeTempNorm(biome) + BiomeUtils.getBiomeTempNorm(biomeNorth) + BiomeUtils.getBiomeTempNorm(biomeSouth) + BiomeUtils.getBiomeTempNorm(biomeEast) + BiomeUtils.getBiomeTempNorm(biomeWest)) / 5.0F);
        
        //Denormalize, multiply by the max temp offset, add to the current temp
        int newTemperatureLevel = initialTemperature.getRawValue();
        
        if (!(biomeTemp < 0.65F && biomeTemp > 0.15F))
        {
            int temperatureModifier = (int)Math.round((biomeTemp * 2.0F - 1.0F) * ModConfig.temperature.maxBiomeTempOffset);
            
            // Apply underground coefficient
            if (world.provider.isSurfaceWorld() && ModConfig.temperature.enableUndergroundEffect)
                temperatureModifier = Math.round(TerrainUtils.getAverageUndergroundCoefficient(world, pos) * temperatureModifier);
            newTemperatureLevel += temperatureModifier;
        }
        
        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Biome Temperature", initialTemperature, new Temperature(newTemperatureLevel)));
        
        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return false;
    }
}

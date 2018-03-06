package toughasnails.temperature.modifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;
import toughasnails.util.BiomeUtils;
import toughasnails.util.TerrainUtils;

public class TimeModifier extends TemperatureModifier
{
    public TimeModifier(String id)
    {
        super(id);
    }

    @Override
    public Temperature applyEnvironmentModifiers(World world, BlockPos pos, Temperature initialTemperature, IModifierMonitor monitor)
    {
        Biome biome = world.getBiome(pos);
        long worldTime = world.getWorldTime();
        
        float extremityModifier = BiomeUtils.getBiomeTempExtremity(biome);
        //Reaches the highest point during the middle of the day and at midnight. Normalized to be between -1 and 1
        float timeNorm = (-Math.abs(((worldTime + 6000) % 24000.0F) - 12000.0F) + 6000.0F) / 6000.0F;
        
        int temperatureLevel = initialTemperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;

        if (world.provider.isSurfaceWorld() && ((timeNorm < 0 && ModConfig.temperature.enableNightTimeModifier)|| (timeNorm > 0 && ModConfig.temperature.enableDayTimeModifier)))
        {
            int temperatureModifier = (int)(ModConfig.temperature.timeModifier * timeNorm * (Math.max(1.0F, extremityModifier * ModConfig.temperature.timeExtremityMultiplier)));
            
            // Apply underground coefficient
            if (ModConfig.temperature.enableUndergroundEffect)
                temperatureModifier = Math.round(TerrainUtils.getAverageUndergroundCoefficient(world, pos) * temperatureModifier);
            newTemperatureLevel += temperatureModifier;
        }

        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Time", initialTemperature, new Temperature(newTemperatureLevel)));

        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return false;
    }
}

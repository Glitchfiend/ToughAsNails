package toughasnails.temperature.modifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;
import toughasnails.util.BiomeUtils;

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
        float timeNorm = MathHelper.cos((float) (world.getCelestialAngle(worldTime) * 2.0F * Math.PI));
        
        int temperatureLevel = initialTemperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;

        if (world.provider.isSurfaceWorld() && ((timeNorm < 0 && ModConfig.temperature.enableNightTimeModifier)|| (timeNorm > 0 && ModConfig.temperature.enableDayTimeModifier)))
        {
        	newTemperatureLevel += ModConfig.temperature.timeModifier * timeNorm * (Math.max(1.0F, extremityModifier * ModConfig.temperature.timeExtremityMultiplier));
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

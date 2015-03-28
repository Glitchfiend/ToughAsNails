package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale;

public class BiomeModifier implements ITemperatureModifier
{
    public static final int TEMPERATURE_SCALE_MIDPOINT = TemperatureScale.getScaleTotal() / 2;
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        float humidity = biome.rainfall;
        float humidityMultiplier = 2.0F * Math.abs((humidity % 1.0F) - 0.5F);
        
        return changeRate - (int)((10 * humidityMultiplier) * 20);
    }

    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        float biomeTemperature = biome.temperature;
        int newTemperatureLevel = TEMPERATURE_SCALE_MIDPOINT + (int)((biomeTemperature - 0.5F) * 5.0F);
        
        return new TemperatureInfo(newTemperatureLevel);
    }
}

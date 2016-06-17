package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.util.BiomeUtils;

public class BiomeModifier extends TemperatureModifier
{
    public static final int MAX_TEMP_OFFSET = 10;
    
    public BiomeModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        Biome biome = world.getBiomeGenForCoords(player.getPosition());
        float humidity = biome.getRainfall();
        float humidityMultiplier = 2.0F * Math.abs((humidity % 1.0F) - 0.5F);
        int newChangeRate = changeRate - (int)((10 * humidityMultiplier) * 20);
        
        debugger.start(Modifier.BIOME_HUMIDITY_RATE, changeRate);
        debugger.end(newChangeRate);
        
        return newChangeRate;
    }

    @Override
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature)
    {
        Biome biome = world.getBiomeGenForCoords(player.getPosition());
        
        //Denormalize, multiply by the max temp offset, add to the current temp
        int newTemperatureLevel = temperature.getRawValue() + (int)Math.round((BiomeUtils.getBiomeTempNorm(biome) * 2.0F - 1.0F) * MAX_TEMP_OFFSET);
        
        debugger.start(Modifier.BIOME_TEMPERATURE_TARGET, temperature.getRawValue());
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }
}

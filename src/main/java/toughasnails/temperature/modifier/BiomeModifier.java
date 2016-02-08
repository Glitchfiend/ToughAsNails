package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureDebugger.Modifier;

public class BiomeModifier extends TemperatureModifier
{
    public static final int TEMPERATURE_SCALE_MIDPOINT = TemperatureScale.getScaleTotal() / 2;
    public static final int MAX_TEMP_OFFSET = 20;
    
    public BiomeModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        float humidity = biome.rainfall;
        float humidityMultiplier = 2.0F * Math.abs((humidity % 1.0F) - 0.5F);
        int newChangeRate = changeRate - (int)((10 * humidityMultiplier) * 20);
        
        debugger.start(Modifier.BIOME_HUMIDITY_RATE, changeRate);
        debugger.end(newChangeRate);
        
        return newChangeRate;
    }

    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        float normalizedTemp = (MathHelper.clamp_float(biome.temperature, -0.5F, 2.0F) + 0.5F) / 2.5F;
        
        //Denormalize, multiply by the max temp offset, add to the current temp
        int newTemperatureLevel = TEMPERATURE_SCALE_MIDPOINT + (int)Math.round((normalizedTemp * 2.0F - 1.0F) * MAX_TEMP_OFFSET);
        
        debugger.start(Modifier.BIOME_TEMPERATURE_TARGET, temperature.getScalePos());
        debugger.end(newTemperatureLevel);
        
        return new TemperatureInfo(newTemperatureLevel);
    }
}

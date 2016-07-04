package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureTrend;
import toughasnails.util.BiomeUtils;

public class AltitudeModifier extends TemperatureModifier
{
    public static final int ALTITUDE_TARGET_MODIFIER = 3;

    
    public AltitudeModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate, TemperatureTrend trend)
    {
        return changeRate;
    }

    @Override
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature)
    {
        int temperatureLevel = temperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;

        debugger.start(Modifier.ALTITUDE_TARGET, newTemperatureLevel);
        
        if (world.provider.isSurfaceWorld())
        {
        	newTemperatureLevel -= MathHelper.abs_int(MathHelper.floor_double(((64 - player.posY) / 64) * ALTITUDE_TARGET_MODIFIER) + 1);
        }
        
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }
}

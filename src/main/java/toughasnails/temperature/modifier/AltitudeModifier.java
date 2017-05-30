package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;

public class AltitudeModifier extends TemperatureModifier
{
    public static final int ALTITUDE_TARGET_MODIFIER = 3;

    
    public AltitudeModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }

    @Override
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature)
    {
        int temperatureLevel = temperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;

        debugger.start(Modifier.ALTITUDE_TARGET, newTemperatureLevel);
        
        if (world.provider.isSurfaceWorld())
        {
        	newTemperatureLevel -= MathHelper.abs(MathHelper.floor(((64 - player.posY) / 64) * ALTITUDE_TARGET_MODIFIER) + 1);
        }
        
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }
}

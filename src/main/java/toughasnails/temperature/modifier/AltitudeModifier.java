package toughasnails.temperature.modifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;

public class AltitudeModifier extends TemperatureModifier
{
    public AltitudeModifier(String id)
    {
        super(id);
    }

    @Override
    public Temperature applyEnvironmentModifiers(World world, BlockPos pos, Temperature initialTemperature, IModifierMonitor monitor)
    {
        int temperatureLevel = initialTemperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;

        if (world.provider.isSurfaceWorld())
        {
            newTemperatureLevel += -(MathHelper.floor(pos.getY() / ModConfig.temperature.altitudeModifierStep)) + ModConfig.temperature.altitudeModifierOffset;
        }

        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Altitude", initialTemperature, new Temperature(newTemperatureLevel)));
        
        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return false;
    }
}

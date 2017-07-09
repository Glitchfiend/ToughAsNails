package toughasnails.temperature.modifier;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModCompat;
import toughasnails.init.ModConfig;

public class WeatherModifier extends TemperatureModifier
{
    public WeatherModifier(String id)
    {
        super(id);
    }

    @Override
    public Temperature applyPlayerModifiers(EntityPlayer player, Temperature initialTemperature, IModifierMonitor monitor)
    {
        World world = player.world;
        int temperatureLevel = initialTemperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;
        
        BlockPos playerPos = player.getPosition();
        Block block = world.getBlockState(playerPos).getBlock();
        
        if (player.isWet())
        {
            if (block != ModCompat.HOT_SPRING_WATER)
            {
                newTemperatureLevel += ModConfig.temperature.wetModifier;
                monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Wet", initialTemperature, new Temperature(newTemperatureLevel)));
            }
        }
        else if (world.isRaining() && world.canSeeSky(playerPos) && world.getBiome(playerPos).getEnableSnow())
        {
            newTemperatureLevel += ModConfig.temperature.snowModifier;
            monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Snow", initialTemperature, new Temperature(newTemperatureLevel)));
        }

        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return true;
    }
}

package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureDebugger.Modifier;

public class WeatherModifier extends TemperatureModifier
{
    public static final int WET_RATE_MODIFIER = -750;
    public static final int WET_TARGET_MODIFIER = -5;
    public static final int SNOW_TARGET_MODIFIER = -10;
    
    public WeatherModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        int newChangeRate = changeRate;
        
        debugger.start(Modifier.WET_RATE, changeRate);
        
        if (player.isWet())
        {
            newChangeRate += WET_RATE_MODIFIER;
        }
        
        debugger.end(newChangeRate);
        
        return newChangeRate;
    }
    
    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        int temperatureLevel = temperature.getScalePos();
        int newTemperatureLevel = temperatureLevel;
        
        BlockPos playerPos = player.getPosition();
        
        if (player.isWet())
        {
            debugger.start(Modifier.WET_TARGET, newTemperatureLevel);
            newTemperatureLevel += WET_TARGET_MODIFIER;
            debugger.end(newTemperatureLevel);
        }
        else if (world.isRaining() && world.canSeeSky(playerPos) && world.getBiomeGenForCoords(playerPos).getEnableSnow())
        {
            debugger.start(Modifier.SNOW_TARGET, newTemperatureLevel);
            newTemperatureLevel += SNOW_TARGET_MODIFIER;
            debugger.end(newTemperatureLevel);
        }

        return new TemperatureInfo(newTemperatureLevel);
    }
}

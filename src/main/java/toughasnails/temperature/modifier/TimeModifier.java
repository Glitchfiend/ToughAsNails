package toughasnails.temperature.modifier;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureInfo;

public class TimeModifier extends TemperatureModifier
{
    public static final int TIME_TARGET_MODIFIER = 5;
    
    private Random dayRandom = new Random();
    
    public TimeModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        return changeRate;
    }

    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        int temperatureLevel = temperature.getScalePos();
        int newTemperatureLevel = temperatureLevel;
        long worldTime = world.getWorldTime();
        float timeMultiplier = (-Math.abs(((worldTime + 6000) % 24000.0F) - 12000.0F) + 6000.0F) / 6000.0F;
        
        debugger.start(Modifier.TIME_TARGET, newTemperatureLevel);
        newTemperatureLevel += TIME_TARGET_MODIFIER * timeMultiplier;
        debugger.end(newTemperatureLevel);
        
        dayRandom.setSeed((int)(worldTime / 24000));
        
        debugger.start(Modifier.DAY_RANDOM_TARGET, newTemperatureLevel);
        newTemperatureLevel += dayRandom.nextInt(10) - 5;
        debugger.end(newTemperatureLevel);
        
        return new TemperatureInfo(newTemperatureLevel);
    }
}

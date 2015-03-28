package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureInfo;

public class TimeModifier implements ITemperatureModifier
{
    public static final int TIME_TARGET_MODIFIER = 5;
    
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
        
        newTemperatureLevel += TIME_TARGET_MODIFIER * timeMultiplier;
        
        return new TemperatureInfo(newTemperatureLevel);
    }
}

package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureScale.TemperatureRange;

public class WaterModifier implements ITemperatureModifier
{
    public static final int WET_RATE_MODIFIER = -200;
    public static final int WET_TARGET_MODIFIER = -10;
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        if (player.isWet())
        {
            return changeRate + WET_RATE_MODIFIER;
        }
        
        return changeRate;
    }
    
    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        int temperatureLevel = temperature.getScalePos();
        int newTemperatureLevel = temperatureLevel;
        
        if (player.isInWater())
        {
            newTemperatureLevel += WET_TARGET_MODIFIER;
        }

        return new TemperatureInfo(newTemperatureLevel);
    }
}

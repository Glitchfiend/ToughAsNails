package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureScale.TemperatureRange;

public class WaterModifier implements ITemperatureModifier
{
    public static final int CHANGE_RATE_MODIFIER = 200;
    public static final int MAX_COOL_TEMPERATURE = TemperatureScale.getRangeStart(TemperatureRange.COOL) + TemperatureRange.COOL.getRangeSize() - 1;
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        if (player.isInWater())
        {
            return Math.max(0, changeRate - CHANGE_RATE_MODIFIER);
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
            newTemperatureLevel -= 10;
        }

        return new TemperatureInfo(Math.max(0, newTemperatureLevel));
    }
}

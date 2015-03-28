package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureInfo;

public class WeatherModifier implements ITemperatureModifier
{
    public static final int WET_RATE_MODIFIER = -200;
    public static final int WET_TARGET_MODIFIER = -10;
    public static final int SNOW_TARGET_MODIFIER = -15;
    
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
        
        BlockPos playerPos = player.getPosition();
        
        if (player.isWet())
        {
            newTemperatureLevel += WET_TARGET_MODIFIER;
        }
        else if (world.isRaining() && world.canSeeSky(playerPos) && world.getBiomeGenForCoords(playerPos).getEnableSnow())
        {
            newTemperatureLevel += SNOW_TARGET_MODIFIER;
        }

        return new TemperatureInfo(newTemperatureLevel);
    }
}

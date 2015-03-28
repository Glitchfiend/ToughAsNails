package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureInfo;

public class PlayerStateModifier implements ITemperatureModifier
{
    public static final int SPRINTING_RATE_MODIFIER = -200;
    public static final int SPRINTING_TARGET_MODIFIER = 10;
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        int newChangeRate = changeRate;
        
        if (player.isSprinting())
        {
            newChangeRate += SPRINTING_RATE_MODIFIER;
        }
        
        newChangeRate += (player.getHealth() - 20) * 20;
        
        return newChangeRate;
    }

    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        int temperatureLevel = temperature.getScalePos();
        int newTemperatureLevel = temperatureLevel;
        
        if (player.getHealth() <= 10.0F)
        {
            newTemperatureLevel += Math.abs((player.getHealth() - 20.0F)) / 2;
        }
        
        if (player.isSprinting())
        {
            newTemperatureLevel += SPRINTING_TARGET_MODIFIER;
        }
        
        return temperature;
    }

}

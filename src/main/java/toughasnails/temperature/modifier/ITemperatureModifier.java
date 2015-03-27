package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureInfo;

public interface ITemperatureModifier
{
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate);
    public TemperatureInfo modifyTemperature(World world, EntityPlayer player, TemperatureInfo temperature);
}

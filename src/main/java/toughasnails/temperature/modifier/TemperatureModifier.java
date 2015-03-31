package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureInfo;

public abstract class TemperatureModifier
{
    protected final TemperatureDebugger debugger;
    
    protected TemperatureModifier(TemperatureDebugger debugger)
    {
        this.debugger = debugger;
    }
    
    public abstract int modifyChangeRate(World world, EntityPlayer player, int changeRate);
    public abstract TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature);
}

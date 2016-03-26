package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;

public abstract class TemperatureModifier
{
    protected final TemperatureDebugger debugger;
    
    protected TemperatureModifier(TemperatureDebugger debugger)
    {
        this.debugger = debugger;
    }
    
    public abstract int modifyChangeRate(World world, EntityPlayer player, int changeRate);
    public abstract Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature);
}

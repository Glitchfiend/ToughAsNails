package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;

public class PlayerStateModifier extends TemperatureModifier
{
    public static final int SPRINTING_RATE_MODIFIER = 200;
    public static final int SPRINTING_TARGET_MODIFIER = 3;
    
    public PlayerStateModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }

    @Override
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature)
    {
        int temperatureLevel = temperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;
        BlockPos playerPos = player.getPosition();
        
        debugger.start(Modifier.SPRINTING_TARGET, newTemperatureLevel);
        
        if (player.isSprinting())
        {
            newTemperatureLevel += SPRINTING_TARGET_MODIFIER;
        }
        
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }

}

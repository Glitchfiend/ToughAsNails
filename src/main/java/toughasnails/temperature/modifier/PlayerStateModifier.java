package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;

public class PlayerStateModifier extends TemperatureModifier
{
    public PlayerStateModifier(String id)
    {
        super(id);
    }

    @Override
    public Temperature applyPlayerModifiers(EntityPlayer player, Temperature initialTemperature, IModifierMonitor monitor)
    {
        int temperatureLevel = initialTemperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;
        BlockPos playerPos = player.getPosition();

        if (player.isSprinting())
        {
            newTemperatureLevel += ModConfig.temperature.sprintingModifier;
        }

        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Sprinting", initialTemperature, new Temperature(newTemperatureLevel)));
        
        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return true;
    }

}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.config.ServerConfig;

public class TemperatureHandler
{
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        Player player = event.player;

        if (!ServerConfig.enableTemperature.get() || player.level.isClientSide())
            return;

        TemperatureLevel temperature = TemperatureHelper.getPlayerTemperature(player);
        int frozenTicks = player.getTicksFrozen();
        int ticksToFreeze = player.getTicksRequiredToFreeze() + 2; // Add 2 to cause damage

        if (temperature == TemperatureLevel.ICY && frozenTicks < ticksToFreeze)
        {
            // Increment ticks frozen by 3 to overcome thawing.
            player.setTicksFrozen(Math.min(ticksToFreeze, player.getTicksFrozen() + 3));
        }
    }
}

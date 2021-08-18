/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class TemperatureHooks
{
    /*
     * Hooks called by ASM
     */
    public static void onAiStepRemoveFrost(LivingEntity entity)
    {
        if (entity instanceof Player && !entity.level.isClientSide && !entity.isDeadOrDying())
        {
            Player player = (Player)entity;
            if (!entity.isInPowderSnow || !entity.canFreeze())
            {
                if (TemperatureHelper.getPlayerTemperature(player) == TemperatureLevel.ICY)
                {
                    // Add 3 ticks to negate the 2 subtracted earlier in aiStep
                    player.setTicksFrozen(entity.getTicksFrozen() + 3);
                }
            }
        }
    }
}

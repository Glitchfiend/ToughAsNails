/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.world.entity.player.Player;

public interface IPlayerTemperatureModifier
{
    TemperatureLevel modify(Player player, TemperatureLevel current);
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IPositionalTemperatureModifier
{
    TemperatureLevel modify(Level level, BlockPos pos, TemperatureLevel current);
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IProximityBlockModifier {

    Type getProximityType(Level level, BlockPos pos, BlockState state);

    enum Type
    {
        HEATING, COOLING, NONE
    }

}

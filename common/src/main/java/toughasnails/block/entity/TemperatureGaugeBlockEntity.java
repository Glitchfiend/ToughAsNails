/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import toughasnails.api.blockentity.TANBlockEntityTypes;

public class TemperatureGaugeBlockEntity extends BlockEntity
{
    public TemperatureGaugeBlockEntity(BlockPos pos, BlockState state) {
        super(TANBlockEntityTypes.TEMPERATURE_GAUGE, pos, state);
    }
}

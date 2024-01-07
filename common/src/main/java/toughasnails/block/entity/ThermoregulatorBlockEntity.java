/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import toughasnails.api.blockentity.TANBlockEntityTypes;

public class ThermoregulatorBlockEntity extends BlockEntity
{
    public ThermoregulatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(TANBlockEntityTypes.THERMOREGULATOR, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ThermoregulatorBlockEntity blockEntity)
    {

    }
}

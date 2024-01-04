/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.ToIntFunction;

public class ThermoregulatorBlock extends Block
{
    public static final BooleanProperty COOLING = BooleanProperty.create("cooling");
    public static final BooleanProperty HEATING = BooleanProperty.create("heating");

    public ThermoregulatorBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COOLING, false).setValue(HEATING, false));
    }

    public static ToIntFunction<BlockState> lightLevel(int level)
    {
        return (state) -> {
            boolean cooling = state.getValue(COOLING);
            boolean heating = state.getValue(HEATING);

            return ((cooling ? 1: 0) + (heating ? 1: 0)) * level;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(COOLING, HEATING);
    }
}

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureLevel;

public class TemperatureData implements ITemperature
{
    public static final TemperatureLevel DEFAULT_LEVEL = TemperatureLevel.NEUTRAL;

    private TemperatureLevel level = DEFAULT_LEVEL;
    private int ticksHyperthermic;

    @Override
    public TemperatureLevel getLevel()
    {
        return this.level;
    }

    @Override
    public int getTicksHyperthermic()
    {
        return this.ticksHyperthermic;
    }

    @Override
    public void setLevel(TemperatureLevel level)
    {
        this.level = level;
    }

    @Override
    public void setTicksHyperthermic(int ticks)
    {
        this.ticksHyperthermic = ticks;
    }
}

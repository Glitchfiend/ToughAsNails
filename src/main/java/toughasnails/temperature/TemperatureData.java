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
    private TemperatureLevel targetLevel = DEFAULT_LEVEL;
    private int positionalChangeDelayTicks;
    private int ticksHyperthermic;
    private int extremityDelayTicks;

    private TemperatureLevel lastTemperature = DEFAULT_LEVEL;
    private int lastHyperthermiaTicks;

    @Override
    public TemperatureLevel getLevel()
    {
        return this.level;
    }

    @Override
    public int getHyperthermiaTicks()
    {
        return this.ticksHyperthermic;
    }

    @Override
    public int getExtremityDelayTicks()
    {
        return this.extremityDelayTicks;
    }

    @Override
    public TemperatureLevel getLastLevel()
    {
        return this.lastTemperature;
    }

    @Override
    public TemperatureLevel getTargetLevel()
    {
        return this.targetLevel;
    }

    @Override
    public int getChangeDelayTicks()
    {
        return this.positionalChangeDelayTicks;
    }

    @Override
    public int getLastHyperthermiaTicks()
    {
        return this.lastHyperthermiaTicks;
    }

    @Override
    public void setLevel(TemperatureLevel level)
    {
        this.level = level;
    }

    @Override
    public void setHyperthermiaTicks(int ticks)
    {
        this.ticksHyperthermic = ticks;
    }

    @Override
    public void setExtremityDelayTicks(int ticks)
    {
        this.extremityDelayTicks = ticks;
    }

    @Override
    public void setLastLevel(TemperatureLevel level)
    {
        this.lastTemperature = level;
    }

    @Override
    public void setTargetLevel(TemperatureLevel level)
    {
        this.targetLevel = level;
    }

    @Override
    public void setChangeDelayTicks(int ticks)
    {
        this.positionalChangeDelayTicks = ticks;
    }

    @Override
    public void setLastHyperthermiaTicks(int ticks)
    {
        this.lastHyperthermiaTicks = ticks;
    }
}

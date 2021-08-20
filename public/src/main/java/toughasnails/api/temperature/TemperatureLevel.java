/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.util.Mth;

public enum TemperatureLevel
{
    ICY, COLD, NEUTRAL, WARM, HOT;

    public TemperatureLevel increment(int amount)
    {
        return TemperatureLevel.values()[Mth.clamp(this.ordinal() + amount, 0, TemperatureLevel.values().length - 1)];
    }

    public TemperatureLevel decrement(int amount)
    {
        return increment(-amount);
    }
}

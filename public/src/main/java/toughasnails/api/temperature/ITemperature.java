/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

public interface ITemperature
{
    /**
     * Get the temperature level.
     * @return The temperature level.
     */
    TemperatureLevel getLevel();

    /**
     * Set the temperature level.
     * @param temperature temperature level
     */
    void setLevel(TemperatureLevel level);
}

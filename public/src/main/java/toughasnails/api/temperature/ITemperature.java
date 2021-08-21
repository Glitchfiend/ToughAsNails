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
     * Get the number of ticks of hyperthermia.
     * @return number of ticks.
     */
    int getTicksHyperthermic();

    /**
     * Set the temperature level.
     * @param temperature temperature level
     */
    void setLevel(TemperatureLevel level);

    /**
     * Set the number of ticks of hyperthermia.
     * @param ticks number of ticks.
     */
    void setTicksHyperthermic(int ticks);
}

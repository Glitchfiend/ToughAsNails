/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

public interface ITemperature
{
    /**
     * Get the temperature level.
     * @return temperature level.
     */
    TemperatureLevel getLevel();

    /**
     * Get the number of ticks of hyperthermia.
     * @return number of ticks.
     */
    int getHyperthermiaTicks();

    /**
     * Get the number of extremity delay ticks.
     * @return number of ticks.
     */
    int getExtremityDelayTicks();

    /**
     * Get the last temperature level.
     * @return temperature level.
     */
    TemperatureLevel getLastLevel();

    /**
     * Get the last number of ticks of hyperthermia.
     * @return number of ticks.
     */
    int getLastHyperthermiaTicks();

    /**
     * Set the temperature level.
     * @param temperature temperature level
     */
    void setLevel(TemperatureLevel level);

    /**
     * Set the number of ticks of hyperthermia.
     * @param ticks number of ticks.
     */
    void setHyperthermiaTicks(int ticks);

    /**
     * Set the number of extremity delay ticks.
     * @param ticks number of ticks.
     */
    void setExtremityDelayTicks(int ticks);

    /**
     * Set the last temperature level.
     * @param level temperature level.
     */
    void setLastLevel(TemperatureLevel level);

    /**
     * Set the last number of hyperthermia ticks.
     * @param ticks number of ticks.
     */
    void setLastHyperthermiaTicks(int ticks);
}

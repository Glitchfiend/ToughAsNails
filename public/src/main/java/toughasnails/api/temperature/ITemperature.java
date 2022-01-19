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
     * Get the last positional temperature level.
     * @return temperature level.
     */
    TemperatureLevel getPositionalLevel();

    /**
     * Get the target positional temperature level.
     * @return temperature level.
     */
    TemperatureLevel getTargetPositionalLevel();

    /**
     * Get the number of ticks to delay changing the positional temperature.
     * @return number of ticks.
     */
    int getPositionalChangeDelayTicks();

    /**
     * Get the last number of ticks of hyperthermia.
     * @return number of ticks.
     */
    int getLastHyperthermiaTicks();

    /**
     * Set the temperature level.
     * @param level temperature level
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
     * Set the last positional temperature level.
     * @param level temperature level.
     */
    void setPositionalLevel(TemperatureLevel level);

    /**
     * Set the target positional temperature level.
     * @param level temperature level.
     */
    void setTargetPositionalLevel(TemperatureLevel level);

    /**
     * Set the number of ticks to delay changing the positional temperature.
     */
    void setPositionalChangeDelayTicks(int ticks);

    /**
     * Set the last number of hyperthermia ticks.
     * @param ticks number of ticks.
     */
    void setLastHyperthermiaTicks(int ticks);
}

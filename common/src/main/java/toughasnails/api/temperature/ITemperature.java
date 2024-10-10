/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.core.BlockPos;

import java.util.Set;

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
     * Get the number of ticks since water was touched.
     * @return number of ticks.
     */
    int getDryTicks();

    /**
     * Get the last temperature level.
     * @return temperature level.
     */
    TemperatureLevel getLastLevel();

    /**
     * Get the target temperature level.
     * @return temperature level.
     */
    TemperatureLevel getTargetLevel();

    /**
     * Get the number of ticks to delay changing the temperature.
     * @return number of ticks.
     */
    int getChangeDelayTicks();

    /**
     * Get the last number of ticks of hyperthermia.
     * @return number of ticks.
     */
    int getLastHyperthermiaTicks();

    /**
     * Get the last positions of nearby thermoregulators.
     * @return nearby thermoregulators.
     */
    Set<BlockPos> getLastNearbyThermoregulators();

    /**
     * Get the positions of nearby thermoregulators.
     * @return nearby thermoregulators.
     */
    Set<BlockPos> getNearbyThermoregulators();

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
     * Set the number of ticks since water was touched.
     * @return number of ticks.
     */
    void setDryTicks(int ticks);


    /**
     * Set the last temperature level.
     * @param level temperature level.
     */
    void setLastLevel(TemperatureLevel level);

    /**
     * Set the target temperature level.
     * @param level temperature level.
     */
    void setTargetLevel(TemperatureLevel level);

    /**
     * Set the number of ticks to delay changing the temperature.
     */
    void setChangeDelayTicks(int ticks);

    /**
     * Set the last number of hyperthermia ticks.
     * @param ticks number of ticks.
     */
    void setLastHyperthermiaTicks(int ticks);

    /**
     * Set the last nearby thermoregulators.
     * @param values nearby thermoregulators.
     */
    void setLastNearbyThermoregulators(Set<BlockPos> values);

    /**
     * Set the nearby thermoregulators.
     * @param values nearby thermoregulators.
     */
    void setNearbyThermoregulators(Set<BlockPos> values);
}

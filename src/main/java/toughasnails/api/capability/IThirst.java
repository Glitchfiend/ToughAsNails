/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.capability;

public interface IThirst
{
    /**
     * Get the level of thirst ranging from 0 to 20.
     * @return The thirst level.
     */
    int getThirstLevel();

    /**
     * Get the number of thirst ticks.
     * @return The number of thirst ticks.
     */
    int getTickTimer();

    /**
     * Add ticks to the tick timer
     * @param ticks The number of thirst ticks.
     */
    void addTicks(int ticks);

    /**
     * Get the current hydration level.
     * @return The hydration level.
     */
    float getHydrationLevel();

    /**
     * Get the level of thirst exhaustion ranging from 0 to 40.
     * @return The level of exhaustion.
     */
    float getExhaustionLevel();

    /**
     * Set the thirst level.
     * @param level thirst level
     */
    void setThirstLevel(int level);

    /**
     * Set the tick timer.
     * @param timer tick timer
     */
    void setTickTimer(int timer);

    /**
     * Set the hydration level.
     * @param hydration hydration level.
     */
    void setHydrationLevel(float hydration);

    /**
     * Set the exhaustion level.
     * @param exhaustion exhaustion level.
     */
    void setExhaustionLevel(float exhaustion);

    /**
     * Add to the exhaustion level.
     * @param exhaustion amount of exhaustion to add.
     */
    void addExhaustion(float exhaustion);
}

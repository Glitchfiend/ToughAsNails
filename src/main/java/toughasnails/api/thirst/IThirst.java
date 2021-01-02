/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.thirst;

public interface IThirst
{
    /**
     * Get the level of thirst ranging from 0 to 20.
     * @return The thirst level.
     */
    int getThirst();

    /**
     * Get the number of thirst ticks.
     * @return The number of thirst ticks.
     */
    int getTickTimer();

    /**
     * Get the current hydration level.
     * @return The hydration level.
     */
    float getHydration();

    /**
     * Get the level of thirst exhaustion ranging from 0 to 40.
     * @return The level of exhaustion.
     */
    float getExhaustion();

    /**
     * Set the thirst level.
     * @param level thirst level
     */
    void setThirst(int level);

    /**
     * Add to the thirst level
     * @param thirst amount to add to the thirst level.
     */
    void addThirst(int thirst);

    /**
     * Set the tick timer.
     * @param timer tick timer
     */
    void setTickTimer(int timer);

    /**
     * Add ticks to the tick timer
     * @param ticks The number of thirst ticks.
     */
    void addTicks(int ticks);

    /**
     * Set the hydration level.
     * @param hydration hydration level.
     */
    void setHydration(float hydration);

    /**
     * Add to the hydration level.
     * @param hydration amount of hydration to add.
     */
    void addHydration(float hydration);

    /**
     * Set the exhaustion level.
     * @param exhaustion exhaustion level.
     */
    void setExhaustion(float exhaustion);

    /**
     * Add to the exhaustion level.
     * @param exhaustion amount of exhaustion to add.
     */
    void addExhaustion(float exhaustion);

    /**
     * Checks whether the player is thirsty or not.
     * @return whether the player is thirsty.
     */
    boolean isThirsty();
}

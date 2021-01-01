/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.thirst;

import toughasnails.api.capability.IThirst;

public class ThirstData implements IThirst
{
    private int thirstLevel = 20;
    private float hydrationLevel = 5.0F;
    private float exhaustionLevel;
    private int tickTimer;

    @Override
    public int getThirstLevel()
    {
        return this.thirstLevel;
    }

    @Override
    public int getTickTimer()
    {
        return this.tickTimer;
    }

    @Override
    public float getHydrationLevel()
    {
        return this.hydrationLevel;
    }

    @Override
    public float getExhaustionLevel()
    {
        return this.exhaustionLevel;
    }

    @Override
    public void setThirstLevel(int level)
    {
        this.thirstLevel = level;
    }

    @Override
    public void setTickTimer(int timer)
    {
        this.tickTimer = timer;
    }

    @Override
    public void addTicks(int ticks)
    {
        this.tickTimer += ticks;
    }

    @Override
    public void setHydrationLevel(float hydration)
    {
        this.hydrationLevel = hydration;
    }

    @Override
    public void setExhaustionLevel(float exhaustion)
    {
        this.exhaustionLevel = exhaustion;
    }

    @Override
    public void addExhaustion(float exhaustion)
    {
        this.exhaustionLevel += exhaustion;
    }
}

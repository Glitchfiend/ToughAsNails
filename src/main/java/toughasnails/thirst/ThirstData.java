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
    public int getThirst()
    {
        return this.thirstLevel;
    }

    @Override
    public int getTickTimer()
    {
        return this.tickTimer;
    }

    @Override
    public float getHydration()
    {
        return this.hydrationLevel;
    }

    @Override
    public float getExhaustion()
    {
        return this.exhaustionLevel;
    }

    @Override
    public void setThirst(int level)
    {
        this.thirstLevel = level;
    }

    @Override
    public void addThirst(int thirst)
    {
        this.thirstLevel += thirst;
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
    public void setHydration(float hydration)
    {
        this.hydrationLevel = hydration;
    }

    @Override
    public void addHydration(float hydration)
    {
        this.hydrationLevel += hydration;
    }

    @Override
    public void setExhaustion(float exhaustion)
    {
        this.exhaustionLevel = exhaustion;
    }

    @Override
    public void addExhaustion(float exhaustion)
    {
        this.exhaustionLevel += exhaustion;
    }

    @Override
    public boolean isThirsty()
    {
        return this.thirstLevel < 20;
    }
}

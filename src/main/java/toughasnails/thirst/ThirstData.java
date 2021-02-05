/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import toughasnails.api.thirst.IThirst;

public class ThirstData implements IThirst
{
    public static final int DEFAULT_THIRST = 20;
    public static final float DEFAULT_HYDRATION = 5.0F;

    private int thirstLevel = DEFAULT_THIRST;
    private float hydrationLevel = DEFAULT_HYDRATION;
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
        this.thirstLevel = Math.min(this.thirstLevel + thirst, 20);
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

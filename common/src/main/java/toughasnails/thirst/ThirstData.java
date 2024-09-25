/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.nbt.CompoundTag;
import toughasnails.api.thirst.IThirst;
import toughasnails.init.ModConfig;

public class ThirstData implements IThirst
{
    public static final int DEFAULT_THIRST = 20;
    public static final float DEFAULT_HYDRATION = 2.0F;

    private int thirstLevel = DEFAULT_THIRST;
    private float hydrationLevel = DEFAULT_HYDRATION;
    private float exhaustionLevel;
    private int tickTimer;
    private int lastThirst = -99999999;
    private boolean lastHydrationZero = true;

    public void addAdditionalSaveData(CompoundTag nbt)
    {
        if (ModConfig.thirst.enableThirst())
        {
            nbt.putInt("thirstLevel", this.getThirst());
            nbt.putInt("thirstTickTimer", this.getTickTimer());
            nbt.putFloat("thirstHydrationLevel", this.getHydration());
            nbt.putFloat("thirstExhaustionLevel", this.getExhaustion());
        }
        else
        {
            // Save default values
            nbt.putInt("thirstLevel", ThirstData.DEFAULT_THIRST);
            nbt.putInt("thirstTickTimer", 0);
            nbt.putFloat("thirstHydrationLevel", ThirstData.DEFAULT_HYDRATION);
            nbt.putFloat("thirstExhaustionLevel", 0.0F);
        }
    }

    public void readAdditionalSaveData(CompoundTag nbt)
    {
        if (nbt.contains("thirstLevel", 99))
        {
            if (ModConfig.thirst.enableThirst())
            {
                this.setThirst(nbt.getInt("thirstLevel"));
                this.setTickTimer(nbt.getInt("thirstTickTimer"));
                this.setHydration(nbt.getFloat("thirstHydrationLevel"));
                this.setExhaustion(nbt.getFloat("thirstExhaustionLevel"));
            }
            else
            {
                // Use default values if thirst is disabled
                this.setThirst(ThirstData.DEFAULT_THIRST);
                this.setTickTimer(0);
                this.setHydration(ThirstData.DEFAULT_HYDRATION);
                this.setExhaustion(0.0F);
            }
        }
    }

    @Override
    public int getThirst()
    {
        return this.thirstLevel;
    }

    @Override
    public int getLastThirst()
    {
        return this.lastThirst;
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
    public boolean getLastHydrationZero()
    {
        return this.lastHydrationZero;
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
    public void setLastThirst(int thirst)
    {
        this.lastThirst = thirst;
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
    public void setLastHydrationZero(boolean value)
    {
        this.lastHydrationZero = value;
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
    public void drink(int thirst, float hydrationModifier)
    {
        if (!this.isThirsty())
            return;

        this.thirstLevel = Math.min(thirst + this.thirstLevel, 20);
        this.hydrationLevel = Math.min(this.hydrationLevel + (float)thirst * hydrationModifier * 2.0F, (float)this.thirstLevel);
    }

    @Override
    public boolean isThirsty()
    {
        return this.thirstLevel < 20;
    }
}

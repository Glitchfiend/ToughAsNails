/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    public void addAdditionalSaveData(ValueOutput output)
    {
        if (ModConfig.thirst.enableThirst)
        {
            output.putInt("thirstLevel", this.getThirst());
            output.putInt("thirstTickTimer", this.getTickTimer());
            output.putFloat("thirstHydrationLevel", this.getHydration());
            output.putFloat("thirstExhaustionLevel", this.getExhaustion());
        }
        else
        {
            // Save default values
            output.putInt("thirstLevel", ThirstData.DEFAULT_THIRST);
            output.putInt("thirstTickTimer", 0);
            output.putFloat("thirstHydrationLevel", ThirstData.DEFAULT_HYDRATION);
            output.putFloat("thirstExhaustionLevel", 0.0F);
        }
    }

    public void readAdditionalSaveData(ValueInput input)
    {
        if (ModConfig.thirst.enableThirst)
        {
            this.setThirst(input.getInt("thirstLevel").orElse(ThirstData.DEFAULT_THIRST));
            this.setTickTimer(input.getInt("thirstTickTimer").orElse(0));
            this.setHydration(input.getFloatOr("thirstHydrationLevel", ThirstData.DEFAULT_HYDRATION));
            this.setExhaustion(input.getFloatOr("thirstExhaustionLevel", 0.0F));
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

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import toughasnails.api.thirst.IThirst;
import toughasnails.init.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThirstCapabilityProvider implements INBTSerializable<CompoundTag>, ICapabilityProvider
{
    private Capability<IThirst> capability;
    private IThirst instance;

    public ThirstCapabilityProvider(Capability capability, IThirst instance)
    {
        this.capability = capability;
        this.instance = instance;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return cap == this.capability ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag compound = new CompoundTag();

        if (ModConfig.thirst.enableThirst)
        {
            compound.putInt("thirstLevel", instance.getThirst());
            compound.putInt("thirstTickTimer", instance.getTickTimer());
            compound.putFloat("thirstHydrationLevel", instance.getHydration());
            compound.putFloat("thirstExhaustionLevel", instance.getExhaustion());
        }
        else
        {
            // Save default values
            compound.putInt("thirstLevel", ThirstData.DEFAULT_THIRST);
            compound.putInt("thirstTickTimer", 0);
            compound.putFloat("thirstHydrationLevel", ThirstData.DEFAULT_HYDRATION);
            compound.putFloat("thirstExhaustionLevel", 0.0F);
        }

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        if (nbt.contains("thirstLevel", 99))
        {
            if (ModConfig.thirst.enableThirst)
            {
                instance.setThirst(nbt.getInt("thirstLevel"));
                instance.setTickTimer(nbt.getInt("thirstTickTimer"));
                instance.setHydration(nbt.getFloat("thirstHydrationLevel"));
                instance.setExhaustion(nbt.getFloat("thirstExhaustionLevel"));
            }
            else
            {
                // Use default values if thirst is disabled
                instance.setThirst(ThirstData.DEFAULT_THIRST);
                instance.setTickTimer(0);
                instance.setHydration(ThirstData.DEFAULT_HYDRATION);
                instance.setExhaustion(0.0F);
            }
        }
    }
}

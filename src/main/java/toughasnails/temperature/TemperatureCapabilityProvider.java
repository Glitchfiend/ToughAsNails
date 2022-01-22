/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.config.ServerConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TemperatureCapabilityProvider implements INBTSerializable<CompoundTag>, ICapabilityProvider
{
    private Capability<ITemperature> capability;
    private ITemperature instance;

    public TemperatureCapabilityProvider(Capability capability, ITemperature instance)
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

        if (ServerConfig.enableTemperature.get())
        {
            compound.putInt("temperatureLevel", instance.getLevel().ordinal());
            compound.putInt("targetTemperatureLevel", instance.getTargetLevel().ordinal());
            compound.putInt("changeDelayTicks", instance.getChangeDelayTicks());
            compound.putInt("hyperthermiaTicks", instance.getHyperthermiaTicks());
            compound.putInt("extremityDelayTicks", instance.getExtremityDelayTicks());
        }
        else
        {
            // Save default values
            compound.putInt("temperatureLevel", TemperatureData.DEFAULT_LEVEL.ordinal());
            compound.putInt("targetTemperatureLevel", TemperatureData.DEFAULT_LEVEL.ordinal());
            compound.putInt("changeDelayTicks", 0);
            compound.putInt("hyperthermiaTicks", 0);
            compound.putInt("extremityDelayTicks", 0);
        }

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        if (nbt.contains("temperatureLevel", 99))
        {
            if (ServerConfig.enableTemperature.get())
            {
                instance.setLevel(TemperatureLevel.values()[nbt.getInt("temperatureLevel")]);
                instance.setTargetLevel(TemperatureLevel.values()[nbt.getInt("targetTemperatureLevel")]);
                instance.setChangeDelayTicks(nbt.getInt("changeDelayTicks"));
                instance.setHyperthermiaTicks(nbt.getInt("hyperthermiaTicks"));
                instance.setExtremityDelayTicks(nbt.getInt("extremityDelayTicks"));
            }
            else
            {
                // Use default values if temperature is disabled
                instance.setLevel(TemperatureData.DEFAULT_LEVEL);
                instance.setTargetLevel(TemperatureData.DEFAULT_LEVEL);
                instance.setChangeDelayTicks(0);
                instance.setHyperthermiaTicks(0);
                instance.setExtremityDelayTicks(0);
            }
        }
    }
}

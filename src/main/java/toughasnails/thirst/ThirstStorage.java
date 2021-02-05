/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.core.jmx.Server;
import toughasnails.api.thirst.IThirst;
import toughasnails.config.ServerConfig;

import javax.annotation.Nullable;

public class ThirstStorage implements Capability.IStorage<IThirst>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IThirst> capability, IThirst instance, Direction side)
    {
        CompoundNBT compound = new CompoundNBT();

        if (ServerConfig.enableThirst.get())
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
    public void readNBT(Capability<IThirst> capability, IThirst instance, Direction side, INBT nbt)
    {
        if (!(nbt instanceof CompoundNBT))
            throw new IllegalArgumentException("Thirst data must be a CompoundNBT!");

        CompoundNBT compound = (CompoundNBT)nbt;

        if (compound.contains("thirstLevel", 99))
        {
            if (ServerConfig.enableThirst.get())
            {
                instance.setThirst(compound.getInt("thirstLevel"));
                instance.setTickTimer(compound.getInt("thirstTickTimer"));
                instance.setHydration(compound.getFloat("thirstHydrationLevel"));
                instance.setExhaustion(compound.getFloat("thirstExhaustionLevel"));
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

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import toughasnails.api.thirst.IThirst;

import javax.annotation.Nullable;

public class ThirstStorage implements Capability.IStorage<IThirst>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IThirst> capability, IThirst instance, Direction side)
    {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("thirstLevel", instance.getThirst());
        compound.putInt("thirstTickTimer", instance.getTickTimer());
        compound.putFloat("thirstHydrationLevel", instance.getHydration());
        compound.putFloat("thirstExhaustionLevel", instance.getExhaustion());

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
            instance.setThirst(compound.getInt("thirstLevel"));
            instance.setTickTimer(compound.getInt("thirstTickTimer"));
            instance.setHydration(compound.getFloat("thirstHydrationLevel"));
            instance.setExhaustion(compound.getFloat("thirstExhaustionLevel"));
        }
    }
}

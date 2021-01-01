/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.util.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleCapabilityProvider<T> implements INBTSerializable<CompoundNBT>, ICapabilityProvider
{
    private Capability<T> capability;
    private T instance;

    public SimpleCapabilityProvider(Capability capability, T instance)
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
    public CompoundNBT serializeNBT()
    {
        return (CompoundNBT)this.capability.getStorage().writeNBT(this.capability, this.instance, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        this.capability.getStorage().readNBT(this.capability, this.instance, null, nbt);
    }
}

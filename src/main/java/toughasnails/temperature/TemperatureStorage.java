/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.temperature.Temperature;

public class TemperatureStorage implements IStorage<ITemperature>
{
    @Override
    public NBTBase writeNBT(Capability<ITemperature> capability, ITemperature instance, EnumFacing side) 
    {
        NBTTagCompound compound = new NBTTagCompound();
        
        compound.setInteger("temperatureLevel", instance.getTemperature().getRawValue());
        compound.setInteger("temperatureTimer", instance.getChangeTime());

        return compound;
    }

    @Override
    public void readNBT(Capability<ITemperature> capability, ITemperature instance, EnumFacing side, NBTBase nbt) 
    {
        if (!(nbt instanceof NBTTagCompound)) throw new IllegalArgumentException("Temperature must be read from an NBTTagCompound!");
        
        NBTTagCompound compound = (NBTTagCompound)nbt;
        
        if (compound.hasKey("temperatureLevel"))
        {
            instance.setTemperature(new Temperature(compound.getInteger("temperatureLevel")));
            instance.setChangeTime(compound.getInteger("temperatureTimer"));
        }
    }
}


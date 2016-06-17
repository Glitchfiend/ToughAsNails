/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import toughasnails.api.stat.capability.IThirst;

public class ThirstStorage implements IStorage<IThirst>
{
    @Override
    public NBTBase writeNBT(Capability<IThirst> capability, IThirst instance, EnumFacing side) 
    {
        NBTTagCompound compound = new NBTTagCompound();
        
        compound.setInteger("thirstLevel", instance.getThirst());
        compound.setInteger("thirstTimer", instance.getChangeTime());
        compound.setFloat("thirstHydrationLevel", instance.getHydration());
        compound.setFloat("thirstExhaustionLevel", instance.getExhaustion());

        return compound;
    }

    @Override
    public void readNBT(Capability<IThirst> capability, IThirst instance, EnumFacing side, NBTBase nbt) 
    {
        if (!(nbt instanceof NBTTagCompound)) throw new IllegalArgumentException("Thirst must be read from an NBTTagCompound!");
        
        NBTTagCompound compound = (NBTTagCompound)nbt;
        
        if (compound.hasKey("thirstLevel"))
        {
            instance.setThirst(compound.getInteger("thirstLevel"));
            instance.setHydration(compound.getInteger("thirstHydrationLevel"));
            instance.setExhaustion(compound.getInteger("thirstExhaustionLevel"));
            instance.setChangeTime(compound.getInteger("thirstTimer"));
        }

    }
}


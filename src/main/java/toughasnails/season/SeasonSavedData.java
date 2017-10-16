/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class SeasonSavedData extends WorldSavedData
{
    public static final String DATA_IDENTIFIER = "seasons";
    
    public int seasonCycleTicks;
    public int meltTicks;
    public int snowTicks;
    public static final int MAX_RAINWINDOW = 24000 * 3;	   // Three minecraft days
    
    public SeasonSavedData()
    {
        this(DATA_IDENTIFIER);
    }
    
    //This specific constructor is required for saving to occur
    public SeasonSavedData(String identifier)
    {
        super(identifier);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) 
    {
        this.seasonCycleTicks = nbt.getInteger("SeasonCycleTicks");
        this.meltTicks = nbt.getInteger("MeltTicks");
        this.snowTicks = nbt.getInteger("SnowTicks");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
    {
        nbt.setInteger("SeasonCycleTicks", this.seasonCycleTicks);
        nbt.setInteger("MeltTicks", this.meltTicks);
        nbt.setInteger("SnowTicks", this.snowTicks);
        
        return nbt;
    }
    
    public void shiftSnowWindow(int tickCount, boolean isWinter) {
    	int activeTicks, nonActiveTicks;
    	if( isWinter ) {
    		activeTicks = this.snowTicks;
    		nonActiveTicks = this.meltTicks;
    	}
    	else {
    		nonActiveTicks = this.snowTicks;
    		activeTicks = this.meltTicks;
    	}
    	
    	activeTicks += tickCount;
		if( activeTicks > MAX_RAINWINDOW ) {
			activeTicks = MAX_RAINWINDOW;
			nonActiveTicks = 0;
		}
		else if( activeTicks + nonActiveTicks > MAX_RAINWINDOW ) {
			nonActiveTicks = MAX_RAINWINDOW - activeTicks;
		}
		
    	if( isWinter ) {
    		this.snowTicks = activeTicks;
    		this.meltTicks = nonActiveTicks;
    	}
    	else {
    		this.snowTicks = nonActiveTicks;
    		this.meltTicks = activeTicks;
    	}
    }
}

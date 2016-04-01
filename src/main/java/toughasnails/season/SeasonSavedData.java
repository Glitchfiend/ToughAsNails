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
import toughasnails.api.season.ISeasonData;
import toughasnails.api.season.Season.SubSeason;

public class SeasonSavedData extends WorldSavedData implements ISeasonData
{
    public static final String DATA_IDENTIFIER = "seasons";
    
    public int seasonCycleTicks;
    
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
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) 
    {
        nbt.setInteger("SeasonCycleTicks", this.seasonCycleTicks);
    }

    @Override
    public int getSeasonCycleTicks() 
    {
        return this.seasonCycleTicks;
    }

    //A convenience method to implement ISeasonData, prevents the need to include SeasonTime in
    //the api (potentially enabling stuff to be broken)
    @Override
    public SubSeason getSubSeason() 
    {
        return new SeasonTime(seasonCycleTicks).getSubSeason();
    }
}

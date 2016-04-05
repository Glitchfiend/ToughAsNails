/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.stat.capability;

import toughasnails.api.stat.IPlayerStat;

//TODO: Switch over to using capabilities entirely. In some places it is still assumed that
//this is always implemented by TemperatureStats
public interface IThirst extends IPlayerStat
{
    public void setThirst(int thirst);
    public void setHydration(float hydration);
    public void setExhaustion(float exhaustion);
    public void addStats(int thirst, float hydration);
    
    public int getThirst();
    public float getHydration();
    public float getExhaustion();

    public void setChangeTime(int ticks);
    public int getChangeTime();
}

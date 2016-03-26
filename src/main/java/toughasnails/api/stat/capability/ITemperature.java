/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.stat.capability;

import toughasnails.api.stat.IPlayerStat;
import toughasnails.api.temperature.Temperature;

//TODO: Switch over to using capabilities entirely. In some places it is still assumed that
//this is always implemented by TemperatureStats
public interface ITemperature extends IPlayerStat
{
    public void setTemperature(Temperature temperature);
    public void addTemperature(Temperature difference);
    public Temperature getTemperature();
    
    public void setChangeTime(int ticks);
    public int getChangeTime();
}

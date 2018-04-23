/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.config;

public enum GameplayOption implements ISyncedOption
{
	ENABLE_TEMPERATURE("Enable Body Temperature"),
    ENABLE_THIRST("Enable Thirst"),
    ENABLE_PEACEFUL("Enable Peaceful");
    
    private final String optionName;
    
    private GameplayOption(String name)
    {
        this.optionName = name;
    }
    
    @Override
    public String getOptionName()
    {
        return this.optionName;
    }
}

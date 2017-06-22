/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.config;

public enum TemperatureOption implements ISyncedOption
{
    ENABLE_TEMPERATURE("Enable Body Temperature"),
    BASE_TEMPERATURE_CHANGE_TICKS("Base Temperature Change Ticks"),
    MAX_RATE_MODIFIER("Max Rate Modifier");

    private final String optionName;

    TemperatureOption(String name)
    {
        this.optionName = name;
    }

    @Override
    public String getOptionName()
    {
        return this.optionName;
    }
}

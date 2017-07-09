/*******************************************************************************
 * Copyright 2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.temperature;

import com.google.common.base.Preconditions;

public interface IModifierMonitor
{
    void addEntry(Context context);
    void setTargetTemperature(Temperature temperature);

    class Context
    {
        public final String modifierId;
        public final String description;
        public final Temperature startTemperature;
        public final Temperature endTemperature;

        public Context(String modifierId, String description, Temperature startTemperature, Temperature endTemperature)
        {
            Preconditions.checkArgument(TemperatureHelper.getTemperatureModifiers().containsKey(modifierId) || modifierId.equals("equilibrium") || modifierId.equals("climatisation"), "Modifier id " + modifierId + " does not exist!");
            this.modifierId = modifierId;
            this.description = description;
            this.startTemperature = startTemperature;
            this.endTemperature = endTemperature;
        }

        public ITemperatureModifier getModifier()
        {
            return TemperatureHelper.getTemperatureModifiers().get(this.modifierId);
        }
    }
}

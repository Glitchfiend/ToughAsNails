/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import toughasnails.api.config.TemperatureOption;
import toughasnails.core.ToughAsNails;

import java.io.File;

public class TemperatureConfig extends ConfigHandler
{
    public static final String RATE_SETTINGS = "Rate Settings";
    public static final String MODIFIER_SETTINGS = "Modifier Settings";

    public int altitudeModifier;
    public int jelledSlimeArmorModifier;
    public int woolArmorModifier;
    public int maxBiomeTempOffset;
    public int sprintingModifier;

    public int earlySpringModifier;
    public int midSpringModifier;
    public int lateSpringModifier;
    public int earlySummerModifier;
    public int midSummerModifier;
    public int lateSummerModifier;
    public int earlyAutumnModifier;
    public int midAutumnModifier;
    public int lateAutumnModifier;
    public int earlyWinterModifier;
    public int midWinterModifier;
    public int lateWinterModifier;

    public int timeModifier;
    public float timeExtremityMultiplier;
    public boolean enableDayTimeModifier;
    public boolean enableNightTimeModifier;

    public int wetModifier;
    public int snowModifier;
    
    public int equilibriumDepth;

    public TemperatureConfig(File configFile)
    {
        super(configFile, "Temperature Settings");
    }

    @Override
    protected void loadConfiguration()
    {
        try
        {
            addSyncedValue(TemperatureOption.ENABLE_TEMPERATURE, true, "Toggle", "Players are affected by temperature");
            addSyncedValue(TemperatureOption.BASE_TEMPERATURE_CHANGE_TICKS, 400, RATE_SETTINGS, "The maximum number of ticks before the temperature changes", 20, Integer.MAX_VALUE);
            addSyncedValue(TemperatureOption.MAX_RATE_MODIFIER, 380, RATE_SETTINGS,"The maximum number of ticks to reduce the base rate by", 20, Integer.MAX_VALUE);

            altitudeModifier = config.getInt("Altitude Modifier", MODIFIER_SETTINGS, 3, 0, Integer.MAX_VALUE, "The maximum to increase/decrease temperature by depending on the altitude");
            jelledSlimeArmorModifier = config.getInt("Jelled Slime Armor Modifier", MODIFIER_SETTINGS, -1, Integer.MIN_VALUE, 0, "The amount to decrease the temperature by per unit of jelled slime armor");
            woolArmorModifier = config.getInt("Wool Armor Modifier", MODIFIER_SETTINGS, 1, 0, Integer.MAX_VALUE, "The amount to increase the temperature by per unit of wool armor");
            maxBiomeTempOffset = config.getInt("Max Biome Temperature Modifier", MODIFIER_SETTINGS, 10, 0, Integer.MAX_VALUE, "The maximum to increase/decrease temperature by depending on the biome");
            sprintingModifier = config.getInt("Sprinting Modifier", MODIFIER_SETTINGS, 3, 0, Integer.MAX_VALUE, "The amount to increase the temperature by when sprinting");

            earlySpringModifier = config.getInt("Early Spring Modifier", MODIFIER_SETTINGS, -5, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            midSpringModifier = config.getInt("Mid Spring Modifier", MODIFIER_SETTINGS, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            lateSpringModifier = config.getInt("Late Spring Modifier", MODIFIER_SETTINGS, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            
            earlySummerModifier = config.getInt("Early Summer Modifier", MODIFIER_SETTINGS, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            midSummerModifier = config.getInt("Mid Summer Modifier", MODIFIER_SETTINGS, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            lateSummerModifier = config.getInt("Late Summer Modifier", MODIFIER_SETTINGS, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");

            earlyAutumnModifier = config.getInt("Early Autumn Modifier", MODIFIER_SETTINGS, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            midAutumnModifier = config.getInt("Mid Autumn Modifier", MODIFIER_SETTINGS, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            lateAutumnModifier = config.getInt("Late Autumn Modifier", MODIFIER_SETTINGS, -3, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");

            earlyWinterModifier = config.getInt("Early Winter Modifier", MODIFIER_SETTINGS, -7, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            midWinterModifier = config.getInt("Mid Winter Modifier", MODIFIER_SETTINGS, -15, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");
            lateWinterModifier = config.getInt("Late Winter Modifier", MODIFIER_SETTINGS, -10, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in this sub season");

            timeModifier = config.getInt("Time Modifier", MODIFIER_SETTINGS, 9, 0, Integer.MAX_VALUE, "The amount to increase/decrease the temperature by in the middle of the day/night");
            timeExtremityMultiplier = config.getFloat("Time Extremity Multiplier", MODIFIER_SETTINGS, 1.25F, 0, Float.MAX_VALUE, "The amount to multiply the temperature increment/decrement by in the middle of the day/night based on how extreme the biome temperature is");
            enableDayTimeModifier = config.getBoolean("Enable Day Time Modifier", MODIFIER_SETTINGS, false, "Whether the player's temperature should increase closer to the middle of the day");
            enableNightTimeModifier = config.getBoolean("Enable Night Time Modifier", MODIFIER_SETTINGS, true, "Whether the player's temperature should decrease closer to the middle of the night");

            wetModifier = config.getInt("Wet Modifier", MODIFIER_SETTINGS, -7, Integer.MIN_VALUE, 0, "The amount to decrease the temperature by when wet");
            snowModifier = config.getInt("Snow Modifier", MODIFIER_SETTINGS, -10, Integer.MIN_VALUE, 0, "The amount to decrease the temperature by when snowing");
            
            equilibriumDepth = config.getInt("Equilibrium Depth", MODIFIER_SETTINGS, 20, 0, Integer.MAX_VALUE, "The vertical distance between the surface and the level where surface modifiers are totally absorbed");
        }
        catch (Exception e)
        {
            ToughAsNails.logger.error("Tough As Nails has encountered a problem loading temperature.cfg", e);
        }
        finally
        {
            if (config.hasChanged()) config.save();
        }
    }
}
/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.config;

public enum GameplayOption implements ISyncedOption {
	ENABLE_LOWERED_STARTING_HEALTH("Enable Lowered Starting Health"), //
	ENABLE_THIRST("Enable Thirst"), //
	ENABLE_TEMPERATURE("Enable Body Temperature"), //
	ENABLE_SEASONS("Enable Seasons"), //
	DRINKS("Drinks"), //
	OVERRIDE_THERMOMETER_LIMITS("Override Thermometer Limits"), //
	THERMOMETER_LOWER_BOUND("Thermometer Lower Bound"), //
	THERMOMETER_UPPER_BOUND("Thermometer Upper Bound"), //
	RAIN_CHILL("Enable Rain Chill on World Blocks"), //
	BIOME_TEMP_MODIFIER("Biome Temperature Modification Scaling"), //
	ALTITUDE_TEMP_MODIFIER("Altitude Temperature Modification Scaling"), //
	WET_TEMP_MODIFIER("Temperature Modifier for being Wet"), //
	SNOW_TEMP_MODIFIER("Temperature Modifier for Snow"), //
	TIME_TEMP_MODIFIER("Time of Day Temperature Modification Scaling"), //
	TIME_EXTREMITY_MODIFIER("Temperature Scaling for Time of Day Extremities"), //
	EARLY_AUTUMN_MODIFIER("Temperature Modifier for the EARLY_AUTUMN Season"), //
	MID_AUTUMN_MODIFIER("Temperature Modifier for the MID_AUTUMN Season"), //
	LATE_AUTUMN_MODIFIER("Temperature Modifier for the LATE_AUTUMN Season"), //
	EARLY_WINTER_MODIFIER("Temperature Modifier for the EARLY_WINTER Season"), //
	MID_WINTER_MODIFIER("Temperature Modifier for the MID_WINTER Season"), //
	LATE_WINTER_MODIFIER("Temperature Modifier for the LATE_WINTER Season"), //
	EARLY_SPRING_MODIFIER("Temperature Modifier for the EARLY_SPRING Season"), //
	MID_SPRING_MODIFIER("Temperature Modifier for the MID_SPRING Season"), //
	LATE_SPRING_MODIFIER("Temperature Modifier for the LATE_SPRING Season"), //
	EARLY_SUMMER_MODIFIER("Temperature Modifier for the EARLY_SUMMER Season"), //
	MID_SUMMER_MODIFIER("Temperature Modifier for the MID_SUMMER Season"), //
	LATE_SUMMER_MODIFIER("Temperature Modifier for the LATE_SUMMER Season"), //
	TEMPERATURE_WITHERING("Crops Wither by Temperature"), //
	HIBERNATING("Crops which Hibernate and don't Decay"), //
	CROPS("Crops");

	private final String optionName;

	private GameplayOption(String name) {
		this.optionName = name;
	}

	@Override
	public String getOptionName() {
		return this.optionName;
	}
}

/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.core.ToughAsNails;

public class GameplayConfigurationHandler {
	public static final String SURVIVAL_SETTINGS = "Survival Settings";
	public static final String DRINKS = "Drink Configuration";
	public static final String TEMPERATURE_TWEAKS = "Temperature Tweaks";
	public static final String CROP_TWEAKS = "Crop Tweaks";

	public static Configuration config;

	public static final Map<String, CropGrowConfigEntry> EXTERNAL_DECAYING_CROPS = new HashMap<String, CropGrowConfigEntry>();
	public static final Map<String, CropGrowConfigEntry> EXTERNAL_HIBERNATING_CROPS = new HashMap<String, CropGrowConfigEntry>();

	private static void parseExternalDecayingCrops() {
		List<String> crops = SyncedConfig.getListValue(GameplayOption.CROPS);
		for (String cropEntry : crops) {
			String[] cropData = cropEntry.split(";");
			if (cropData.length == 6) {
				String cropName = cropData[0];
				System.out.println("Parsing crop: " + cropName);
				int minLiving = 0;
				int minOptimal = 0;
				int maxOptimal = 0;
				int maxLiving = 0;
				float nonOptimalChance = 0;
				try {
					minLiving = Integer.parseInt(cropData[1]);
					minOptimal = Integer.parseInt(cropData[2]);
					maxOptimal = Integer.parseInt(cropData[3]);
					maxLiving = Integer.parseInt(cropData[4]);
					nonOptimalChance = Float.parseFloat(cropData[5]);
					CropGrowConfigEntry cropGrowData = new CropGrowConfigEntry(
							minLiving, minOptimal, maxOptimal, maxLiving,
							nonOptimalChance);
					EXTERNAL_DECAYING_CROPS.put(cropName, cropGrowData);
				} catch (NumberFormatException e) {
					ToughAsNails.logger
							.error("Tried to process misconfigured crop! "
									+ cropData.toString());
				}
			}
		}
	}

	private static void parseExternalHibernatingCrops() {
		List<String> hibernating = SyncedConfig
				.getListValue(GameplayOption.HIBERNATING);
		for (String cropEntry : hibernating) {
			String[] cropData = cropEntry.split(";");
			if (cropData.length == 6) {
				String cropName = cropData[0];
				System.out.println("Parsing hibernating crop: " + cropName);
				int minWaking = 0;
				int minOptimal = 0;
				int maxOptimal = 0;
				int maxWaking = 0;
				float nonOptimalChance = 0;
				try {
					minWaking = Integer.parseInt(cropData[1]);
					minOptimal = Integer.parseInt(cropData[2]);
					maxOptimal = Integer.parseInt(cropData[3]);
					maxWaking = Integer.parseInt(cropData[4]);
					nonOptimalChance = Float.parseFloat(cropData[5]);
					CropGrowConfigEntry cropGrowData = new CropGrowConfigEntry(
							minWaking, minOptimal, maxOptimal, maxWaking,
							nonOptimalChance);
					EXTERNAL_HIBERNATING_CROPS.put(cropName, cropGrowData);
				} catch (NumberFormatException e) {
					ToughAsNails.logger
							.error("Tried to process misconfigured hibernating crop! "
									+ cropData.toString());
				}
			}
		}
	}

	public static void init(File configFile) {
		if (config == null) {
			config = new Configuration(configFile);
			loadConfiguration();
		}

		// Parse the external decaying crops list into memory for greater
		// efficiency.
		if (EXTERNAL_DECAYING_CROPS.isEmpty()) {
			parseExternalDecayingCrops();
		}

		// Parse the external hibernating crops list into memory for greater
		// efficiency.
		if (EXTERNAL_HIBERNATING_CROPS.isEmpty()) {
			parseExternalHibernatingCrops();
		}
	}

	private static void loadConfiguration() {
		try {
			// Major features
			addSyncedBool(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH, true,
					SURVIVAL_SETTINGS,
					"Players begin with a lowered maximum health.");
			addSyncedBool(GameplayOption.ENABLE_SEASONS, true,
					SURVIVAL_SETTINGS, "Seasons progress as days increase");
			addSyncedBool(GameplayOption.ENABLE_TEMPERATURE, true,
					SURVIVAL_SETTINGS, "Players are affected by temperature");
			addSyncedBool(GameplayOption.ENABLE_THIRST, true, SURVIVAL_SETTINGS,
					"Players are affected by thirst");

			// Drink list
			String[] drinkDefault = { "minecraft:milk_bucket;*;6;0.4;0.0" };
			addSyncedList(GameplayOption.DRINKS, drinkDefault, DRINKS,
					"List of additional drinks with configurable damage (* = any), thirst, "
							+ "hydration, and poison chance values. ;-delimited");

			// Temperature tweaks
			// Thermometer override
			addSyncedBool(GameplayOption.OVERRIDE_THERMOMETER_LIMITS, false,
					TEMPERATURE_TWEAKS,
					"Override the default TAN thermometer to have upper and lower bounds at the specified limits.");
			addSyncedInt(GameplayOption.THERMOMETER_LOWER_BOUND, -25,
					TEMPERATURE_TWEAKS, "The lower bound of the thermometer.");
			addSyncedInt(GameplayOption.THERMOMETER_UPPER_BOUND, 25,
					TEMPERATURE_TWEAKS, "The upper bound of the thermometer.");

			// Rain-chill
			addSyncedBool(GameplayOption.RAIN_CHILL, true, TEMPERATURE_TWEAKS,
					"Should rain reduce the temperature of a block in the world, or only snow?");

			// Temperature modifiers
			addSyncedInt(GameplayOption.BIOME_TEMP_MODIFIER, 10,
					TEMPERATURE_TWEAKS,
					"Scale how significantly biome influences temperature.");
			addSyncedInt(GameplayOption.ALTITUDE_TEMP_MODIFIER, 3,
					TEMPERATURE_TWEAKS,
					"Scale how significantly altitude influences temperature.");
			addSyncedInt(GameplayOption.WET_TEMP_MODIFIER, -7,
					TEMPERATURE_TWEAKS,
					"Scale how significantly being wet influences temperature.");
			addSyncedInt(GameplayOption.SNOW_TEMP_MODIFIER, -10,
					TEMPERATURE_TWEAKS,
					"Scale how significantly snow influences temperature.");
			addSyncedInt(GameplayOption.TIME_TEMP_MODIFIER, 7,
					TEMPERATURE_TWEAKS,
					"Scale how significantly time of day influences temperature.");
			addSyncedFloat(GameplayOption.TIME_EXTREMITY_MODIFIER, 1.25f,
					TEMPERATURE_TWEAKS,
					"Scale how significantly the extreme times of day change the temperature.");
			addSyncedInt(GameplayOption.EARLY_AUTUMN_MODIFIER, 2,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_AUTUMN season.");
			addSyncedInt(GameplayOption.MID_AUTUMN_MODIFIER, 0,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_AUTUMN season.");
			addSyncedInt(GameplayOption.LATE_AUTUMN_MODIFIER, -2,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_AUTUMN season.");
			addSyncedInt(GameplayOption.EARLY_WINTER_MODIFIER, -4,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_WINTER season.");
			addSyncedInt(GameplayOption.MID_WINTER_MODIFIER, -6,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_WINTER season.");
			addSyncedInt(GameplayOption.LATE_WINTER_MODIFIER, -6,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_WINTER season.");
			addSyncedInt(GameplayOption.EARLY_SPRING_MODIFIER, -4,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_SPRING season.");
			addSyncedInt(GameplayOption.MID_SPRING_MODIFIER, -2,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_SPRING season.");
			addSyncedInt(GameplayOption.LATE_SPRING_MODIFIER, 0,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_SPRING season.");
			addSyncedInt(GameplayOption.EARLY_SUMMER_MODIFIER, 0,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the EARLY_SUMMER season.");
			addSyncedInt(GameplayOption.MID_SUMMER_MODIFIER, 2,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the MID_SUMMER season.");
			addSyncedInt(GameplayOption.LATE_SUMMER_MODIFIER, 4,
					TEMPERATURE_TWEAKS,
					"Set how the temperature is modified in the LATE_SUMMER season.");

			// Crop tweaks
			addSyncedBool(GameplayOption.TEMPERATURE_WITHERING, false,
					CROP_TWEAKS,
					"Should crop withering be based on actual plant temperature or just the season?");

			String[] cropDefault = { "minecraft:wheat;5;10;15;20;0.5" };
			addSyncedList(GameplayOption.CROPS, cropDefault, CROP_TWEAKS,
					"List of crops with configurable min living, "
							+ "min optimal, max optimal, max living temps, "
							+ "and the chance of skipping a growth tick outside "
							+ "of the optimal temperature range. ;-delimited");

			String[] hibernateDefault = {
					"harvestcraft:pamAlmond;5;10;15;20;0.5",
					"harvestcraft:pamApple;5;10;15;20;0.5",
					"harvestcraft:pamApricot;5;10;15;20;0.5",
					"harvestcraft:pamAvocado;5;10;15;20;0.5",
					"harvestcraft:pamBanana;5;10;15;20;0.5",
					"harvestcraft:pamCashew;5;10;15;20;0.5",
					"harvestcraft:pamCherry;5;10;15;20;0.5",
					"harvestcraft:pamChestnut;5;10;15;20;0.5",
					"harvestcraft:pamCinnamon;5;10;15;20;0.5",
					"harvestcraft:pamCoconut;5;10;15;20;0.5",
					"harvestcraft:pamDate;5;10;15;20;0.5",
					"harvestcraft:pamDragonfruit;5;10;15;20;0.5",
					"harvestcraft:pamDurian;5;10;15;20;0.5",
					"harvestcraft:pamFig;5;10;15;20;0.5",
					"harvestcraft:pamGooseberry;5;10;15;20;0.5",
					"harvestcraft:pamGrapefruit;5;10;15;20;0.5",
					"harvestcraft:pamLemon;5;10;15;20;0.5",
					"harvestcraft:pamLime;5;10;15;20;0.5",
					"harvestcraft:pamMango;5;10;15;20;0.5",
					"harvestcraft:pamMaple;5;10;15;20;0.5",
					"harvestcraft:pamNutmeg;5;10;15;20;0.5",
					"harvestcraft:pamOlive;5;10;15;20;0.5",
					"harvestcraft:pamOrange;5;10;15;20;0.5",
					"harvestcraft:pamPapaya;5;10;15;20;0.5",
					"harvestcraft:pamPaperbark;5;10;15;20;0.5",
					"harvestcraft:pamPeach;5;10;15;20;0.5",
					"harvestcraft:pamPear;5;10;15;20;0.5",
					"harvestcraft:pamPecan;5;10;15;20;0.5",
					"harvestcraft:pamPeppercorn;5;10;15;20;0.5",
					"harvestcraft:pamPersimmon;5;10;15;20;0.5",
					"harvestcraft:pamPistachio;5;10;15;20;0.5",
					"harvestcraft:pamPlum;5;10;15;20;0.5",
					"harvestcraft:pamPomegranate;5;10;15;20;0.5",
					"harvestcraft:pamStarfruit;5;10;15;20;0.5",
					"harvestcraft:pamVanillabean;5;10;15;20;0.5",
					"harvestcraft:pamWalnut;5;10;15;20;0.5" };
			addSyncedList(GameplayOption.HIBERNATING, hibernateDefault,
					CROP_TWEAKS,
					"List of hibernating crops with configurable min waking, "
							+ "min optimal, max optimal, max waking temps, "
							+ "and the chance of skipping a growth tick outside "
							+ "of the optimal temperature range. ;-delimited");
		} catch (Exception e) {
			ToughAsNails.logger.error(
					"Tough As Nails has encountered a problem loading gameplay.cfg",
					e);
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}

	private static void addSyncedBool(GameplayOption option,
			boolean defaultValue, String category, String comment) {
		boolean value = config.getBoolean(option.getOptionName(), category,
				defaultValue, comment);
		SyncedConfig.addOption(option, "" + value);
	}

	private static void addSyncedInt(GameplayOption option, int defaultValue,
			String category, String comment) {
		int value = config.getInt(option.getOptionName(), category,
				defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, comment);
		SyncedConfig.addOption(option, "" + value);
	}

	private static void addSyncedFloat(GameplayOption option,
			float defaultValue, String category, String comment) {
		float value = config.getFloat(option.getOptionName(), category,
				defaultValue, Float.MIN_VALUE, Float.MAX_VALUE, comment);
		SyncedConfig.addOption(option, "" + value);
	}

	private static void addSyncedList(GameplayOption option,
			String[] defaultValue, String category, String comment) {
		String[] drinkEntries = config.getStringList(option.getOptionName(),
				category, defaultValue, comment);
		String drinkString = "";
		for (String drinkEntry : drinkEntries) {
			drinkString += (drinkEntry + ",");
		}
		SyncedConfig.addOption(option, drinkString);
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(
			ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(ToughAsNails.MOD_ID)) {
			loadConfiguration();
		}
	}
}

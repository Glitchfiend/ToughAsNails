/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import com.google.common.collect.ImmutableList;
import glitchcore.config.Config;
import glitchcore.util.Environment;
import toughasnails.api.TANAPI;
import toughasnails.core.ToughAsNails;
import toughasnails.temperature.BuiltInTemperatureModifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TemperatureConfig extends Config
{
    public boolean enableTemperature;
    
    public int temperatureChangeDelay;
    public int armorTemperatureChangeDelay;
    public int handheldTemperatureChangeDelay;
    public int playerTemperatureChangeDelay;
    public int extremityReboundTemperatureChangeDelay;
    public int extremityDamageDelay;
    public int climateClemencyDuration;
    public boolean climateClemencyRespawning;

    public int temperatureDropAltitude;
    public int temperatureRiseAltitude;
    public int environmentalModifierAltitude;

    public int nearHeatCoolProximity;

    public int nightHotTemperatureChange;
    public int nightTemperatureChange;

    public int onFireTemperatureChange;
    public int powderSnowTemperatureChange;
    public int wetTemperatureChange;
    public int snowTemperatureChange;
    public int wetTicks;

    public List<String> temperatureModifierOrder;

    public TemperatureConfig()
    {
        super(Environment.getConfigPath().resolve(TANAPI.MOD_ID + "/temperature.toml"));
    }

    private static final List<String> DEFAULT_TEMPERATURE_MODIFIER_ORDER = ImmutableList.of(BuiltInTemperatureModifier.PLAYER_MODIFIERS, BuiltInTemperatureModifier.ITEM_MODIFIER, BuiltInTemperatureModifier.ARMOR_MODIFIER)
        .stream().map(e -> e.toString().toLowerCase()).toList();

    private static final Predicate<List<String>> TEMPERATURE_MODIFIER_VALIDATOR = list -> {
        final var allModifiers = Arrays.stream(BuiltInTemperatureModifier.values()).map(BuiltInTemperatureModifier::toString).toList();
        return list.stream().allMatch(s -> allModifiers.stream().anyMatch(s::equalsIgnoreCase));
    };

    @Override
    public void read()
    {
        // Toggles
        enableTemperature = add("toggles.enable_temperature", true, "Enable or disable temperature.");

        // General options
        temperatureChangeDelay = addNumber("general.temperature_change_delay", 500, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes.");
        armorTemperatureChangeDelay = addNumber("general.armor_temperature_change_delay", 50, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when wearing armor.");
        handheldTemperatureChangeDelay = addNumber("general.handheld_temperature_change_delay", 375, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when holding an item.");
        playerTemperatureChangeDelay = addNumber("general.player_temperature_change_delay", 125, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when affected by a player-based temperature modifier.");
        extremityReboundTemperatureChangeDelay = addNumber("general.extremity_rebound_temperature_change_delay", 250, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when rebounding from an extreme temperature.");
        extremityDamageDelay = addNumber("general.extremity_damage_delay", 500, 0, Integer.MAX_VALUE, "Number of ticks to delay taking damage when icy or hot.");
        climateClemencyDuration = addNumber("general.climate_clemency_duration", 600, 0, Integer.MAX_VALUE, "Number of ticks for the duration of Climate Clemency.");
        climateClemencyRespawning = add("general.climate_clemency_respawning", false, "Whether or not Climate Clemency should be granted when respawning.");
        temperatureModifierOrder = add("general.temperature_modifier_order", DEFAULT_TEMPERATURE_MODIFIER_ORDER, "The order in which to apply built-in temperature modifiers", TEMPERATURE_MODIFIER_VALIDATOR);
        ToughAsNails.LOGGER.info(temperatureModifierOrder);

        // Altitude options
        temperatureDropAltitude = addNumber("altitude.temperature_drop_altitude", 1024, -64, 1024, "Y level to drop the temperature at when above");
        temperatureRiseAltitude = addNumber("altitude.temperature_rise_altitude", -64, -64, 1024, "Y level to rise the temperature at when below");
        environmentalModifierAltitude = addNumber("altitude.environmental_modifier_altitude", 50, -64, 256, "Y level above which environmental modifiers are applied");

        // Blocks options
        nearHeatCoolProximity = addNumber("blocks.near_heat_cool_proximity", 7, 1, 16, "The proximity which constitutes near a heat or cool source");

        // Immersion options
        onFireTemperatureChange = addNumber("immersion.on_fire_temperature_change", 2, -4, 4, "Amount to change the temperature by when on fire.");
        powderSnowTemperatureChange = addNumber("immersion.powdered_snow_temperature_change", -2, -4, 4, "Amount to change the temperature by when in powdered snow.");
        wetTemperatureChange = addNumber("immersion.wet_temperature_change", -1, -4, 4, "Amount to change the temperature by when wet.");
        snowTemperatureChange = addNumber("immersion.snow_temperature_change", -1, -4, 4, "Amount to change the temperature by when snowing.");
        wetTicks = addNumber("immersion.wet_ticks", 40, 0, Integer.MAX_VALUE, "Number of ticks a player stays wet for after touching water, rain or snow.");

        // Time options
        nightTemperatureChange = addNumber("time.night_temperature_change", -1, -4, 4, "Amount to change the temperature at night when the original temperature is not hot.");
        nightHotTemperatureChange = addNumber("time.night_hot_temperature_change", -2, -4, 4, "Amount to change the temperature at night when the original temperature is hot.");
    }
}

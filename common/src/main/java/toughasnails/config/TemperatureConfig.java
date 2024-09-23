/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import com.google.common.collect.ImmutableList;
import toughasnails.glitch.config.Config;
import toughasnails.glitch.util.Environment;
import toughasnails.api.TANAPI;
import toughasnails.temperature.BuiltInTemperatureModifier;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TemperatureConfig extends Config
{
    public boolean enableTemperature;
    
    public int temperatureChangeDelay;
    public int armorTemperatureChangeDelay;
    public int handheldTemperatureChangeDelay;
    public int playerTemperatureChangeDelay;
    public int internalTemperatureChangeDelay;
    public int extremityReboundTemperatureChangeDelay;
    public int extremityDamageDelay;
    public int climateClemencyDuration;
    public boolean climateClemencyRespawning;
    public int consumableEffectDuration;

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

    private static final List<String> DEFAULT_TEMPERATURE_MODIFIER_ORDER = ImmutableList.of(BuiltInTemperatureModifier.PLAYER_MODIFIERS, BuiltInTemperatureModifier.ITEM_MODIFIER, BuiltInTemperatureModifier.ARMOR_MODIFIER, BuiltInTemperatureModifier.INTERNAL_MODIFIER)
        .stream().map(e -> e.toString().toLowerCase()).toList();

    private static final Predicate<List<String>> TEMPERATURE_MODIFIER_VALIDATOR = list -> {
        Set<String> configEntries = list.stream().map(String::toLowerCase).collect(Collectors.toSet());
        Set<String> allModifiers = Arrays.stream(BuiltInTemperatureModifier.values()).map(m -> m.toString().toLowerCase()).collect(Collectors.toSet());
        return configEntries.containsAll(allModifiers) && allModifiers.containsAll(configEntries);
    };

    @Override
    public void load()
    {
        // Toggles
        enableTemperature = add("toggles.enable_temperature", true, "Enable or disable temperature.");

        // General options
        temperatureChangeDelay = addNumber("general.temperature_change_delay", 500, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes.");
        armorTemperatureChangeDelay = addNumber("general.armor_temperature_change_delay", 50, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when wearing armor.");
        handheldTemperatureChangeDelay = addNumber("general.handheld_temperature_change_delay", 375, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when holding an item.");
        playerTemperatureChangeDelay = addNumber("general.player_temperature_change_delay", 125, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when affected by a player-based temperature modifier.");
        internalTemperatureChangeDelay = addNumber("general.internal_temperature_change_delay", 20, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes from consuming a heating or cooling item.");
        extremityReboundTemperatureChangeDelay = addNumber("general.extremity_rebound_temperature_change_delay", 250, 0, Integer.MAX_VALUE, "Number of ticks to delay changing the player's temperature after their temperature changes when rebounding from an extreme temperature.");
        extremityDamageDelay = addNumber("general.extremity_damage_delay", 500, 0, Integer.MAX_VALUE, "Number of ticks to delay taking damage when icy or hot.");
        climateClemencyDuration = addNumber("general.climate_clemency_duration", 6000, 0, Integer.MAX_VALUE, "Number of ticks for the duration of Climate Clemency.");
        climateClemencyRespawning = add("general.climate_clemency_respawning", false, "Whether or not Climate Clemency should be granted when respawning.");
        consumableEffectDuration = add("general.consumable_effect_duration", 1200, "Duration of heating or cooling effects given by consuming items.");
        temperatureModifierOrder = add("general.temperature_modifier_order", DEFAULT_TEMPERATURE_MODIFIER_ORDER, "The order in which to apply built-in temperature modifiers", TEMPERATURE_MODIFIER_VALIDATOR);

        // Altitude options
        temperatureDropAltitude = addNumber("altitude.temperature_drop_altitude", 160, -64, 1024, "Y level to drop the temperature at when above");
        temperatureRiseAltitude = addNumber("altitude.temperature_rise_altitude", -32, -64, 1024, "Y level to rise the temperature at when below");
        environmentalModifierAltitude = addNumber("altitude.environmental_modifier_altitude", 50, -64, 256, "Y level above which environmental modifiers are applied");

        // Blocks options
        nearHeatCoolProximity = addNumber("blocks.near_heat_cool_proximity", 8, 1, 16, "The proximity which constitutes near a heat or cool source");

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

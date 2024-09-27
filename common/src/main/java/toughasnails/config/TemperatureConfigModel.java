package toughasnails.config;

import blue.endless.jankson.Comment;
import com.google.common.collect.ImmutableList;
import io.wispforest.owo.config.Option.SyncMode;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.PredicateConstraint;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.Sync;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import toughasnails.temperature.BuiltInTemperatureModifier;

@Sync(SyncMode.OVERRIDE_CLIENT)
@Config(name = "ToughAsNails_temperature", wrapperName = "TemperatureConfig")
public class TemperatureConfigModel {
  // Toggles
  @Comment("Enable or disable temperature.")
  public boolean enableTemperature = true;

  // General options
  @Comment("Number of ticks to delay changing the player's temperature after their temperature changes.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int temperatureChangeDelay = 500;
  @Comment("Number of ticks to delay changing the player's temperature after their temperature changes when wearing armor.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int armorTemperatureChangeDelay = 50;
  @Comment("Number of ticks to delay changing the player's temperature after their temperature changes when holding an item.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int handheldTemperatureChangeDelay = 375;
  @Comment("Number of ticks to delay changing the player's temperature after their temperature changes when affected by a player-based temperature modifier.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int playerTemperatureChangeDelay = 125;
  @Comment("Number of ticks to delay changing the player's temperature after their temperature changes from consuming a heating or cooling item.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int internalTemperatureChangeDelay = 20;
  @Comment("Number of ticks to delay changing the player's temperature after their temperature changes when rebounding from an extreme temperature.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int extremityReboundTemperatureChangeDelay = 250;
  @Comment("Number of ticks to delay taking damage when icy or hot.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int extremityDamageDelay = 500;
  @Comment("Number of ticks for the duration of Climate Clemency.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int climateClemencyDuration = 6000;
  @Comment("Whether or not Climate Clemency should be granted when respawning.")
  public boolean climateClemencyRespawning = false;
  @Comment("Duration of heating or cooling effects given by consuming items.")
  public int consumableEffectDuration = 1200;
  @Comment("The order in which to apply built-in temperature modifiers")
  @PredicateConstraint("temperatureModifierValidate")
  public List<String> temperatureModifierOrder = ImmutableList.of(
          BuiltInTemperatureModifier.PLAYER_MODIFIERS,
          BuiltInTemperatureModifier.ITEM_MODIFIER,
          BuiltInTemperatureModifier.ARMOR_MODIFIER,
          BuiltInTemperatureModifier.INTERNAL_MODIFIER)
      .stream().map(e -> e.toString().toLowerCase()).toList();
  public static boolean temperatureModifierValidate(List<String> list) {
    Set<String> configEntries = list.stream().map(String::toLowerCase).collect(Collectors.toSet());
    Set<String> allModifiers = Arrays.stream(BuiltInTemperatureModifier.values()).map(m -> m.toString().toLowerCase()).collect(Collectors.toSet());
    return configEntries.containsAll(allModifiers) && allModifiers.containsAll(configEntries);
  }

  // Time options
  @Comment("Amount to change the temperature at night when the original temperature is not hot.")
  @RangeConstraint(min = -4, max = 4)
  public int nightTemperatureChange = -1;
  @Comment("Amount to change the temperature at night when the original temperature is hot.")
  @RangeConstraint(min = -4, max = 4)
  public int nightHotTemperatureChange = -2;

  // Altitude options
  @Comment("Y level to drop the temperature at when above")
  @RangeConstraint(min = -64, max = 1024)
  public int temperatureDropAltitude = 160;
  @Comment("Y level to rise the temperature at when below")
  @RangeConstraint(min = -64, max = 1024)
  public int temperatureRiseAltitude = -32;
  @Comment("Y level above which environmental modifiers are applied")
  @RangeConstraint(min = -64, max = 256)
  public int environmentalModifierAltitude = 50;

  // Blocks options
  @Comment("The proximity which constitutes near a heat or cool source")
  @RangeConstraint(min = 1, max = 16)
  public int nearHeatCoolProximity = 8;

  // Immersion options
  @Comment("Amount to change the temperature by when on fire.")
  @RangeConstraint(min = -4, max = 4)
  public int onFireTemperatureChange = 2;
  @Comment("Amount to change the temperature by when in powdered snow.")
  @RangeConstraint(min = -4, max = 4)
  public int powderSnowTemperatureChange = -2;
  @Comment("Amount to change the temperature by when wet.")
  @RangeConstraint(min = -4, max = 4)
  public int wetTemperatureChange = -1;
  @Comment("Amount to change the temperature by when snowing.")
  @RangeConstraint(min = -4, max = 4)
  public int snowTemperatureChange = -1;
  @Comment("Number of ticks a player stays wet for after touching water, rain or snow.")
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int wetTicks = 40;
}

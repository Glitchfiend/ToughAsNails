package toughasnails.config;

import com.google.common.collect.ImmutableList;
import io.wispforest.owo.config.Option.SyncMode;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.PredicateConstraint;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.Sync;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import toughasnails.api.TANAPI;
import toughasnails.temperature.BuiltInTemperatureModifier;

@Sync(SyncMode.OVERRIDE_CLIENT)
@Modmenu(modId = TANAPI.MOD_ID)
@Config(name = "TAN_temperature", wrapperName = "TemperatureConfig")
public class TemperatureConfigModel {
  // Toggles
  public boolean enableTemperature = true;

  // General options
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int temperatureChangeDelay = 500;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int armorTemperatureChangeDelay = 50;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int handheldTemperatureChangeDelay = 375;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int playerTemperatureChangeDelay = 125;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int internalTemperatureChangeDelay = 20;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int extremityReboundTemperatureChangeDelay = 250;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int extremityDamageDelay = 500;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int climateClemencyDuration = 6000;
  public boolean climateClemencyRespawning = false;
  public int consumableEffectDuration = 1200;
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
  @RangeConstraint(min = -4, max = 4)
  public int nightTemperatureChange = -1;
  @RangeConstraint(min = -4, max = 4)
  public int nightHotTemperatureChange = -2;

  // Altitude options
  @RangeConstraint(min = -64, max = 1024)
  public int temperatureDropAltitude = 160;
  @RangeConstraint(min = -64, max = 1024)
  public int temperatureRiseAltitude = -32;
  @RangeConstraint(min = -64, max = 256)
  public int environmentalModifierAltitude = 50;

  // Blocks options
  @RangeConstraint(min = 1, max = 16)
  public int nearHeatCoolProximity = 8;

  // Immersion options
  @RangeConstraint(min = -4, max = 4)
  public int onFireTemperatureChange = 2;
  @RangeConstraint(min = -4, max = 4)
  public int powderSnowTemperatureChange = -2;
  @RangeConstraint(min = -4, max = 4)
  public int wetTemperatureChange = -1;
  @RangeConstraint(min = -4, max = 4)
  public int snowTemperatureChange = -1;
  @RangeConstraint(min = 0, max = Integer.MAX_VALUE)
  public int wetTicks = 40;
}

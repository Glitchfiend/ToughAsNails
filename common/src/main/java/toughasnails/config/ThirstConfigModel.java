package toughasnails.config;

import io.wispforest.owo.config.Option.SyncMode;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.Sync;

@Sync(SyncMode.OVERRIDE_CLIENT)
@Config(name = "TAN_thirst", wrapperName = "ThirstConfig")
public class ThirstConfigModel {
  public boolean enableThirst = true;
  public boolean enableHandDrinking = false;
  public boolean removeSourceBlocks = false;
  public boolean thirstPreventSprint = true;
  public boolean thirstPreventHealthRegen = true;
  @RangeConstraint(min = 0.0D, max = Double.MAX_VALUE)
  public double thirstExhaustionThreshold = 8.0D;
  @RangeConstraint(min = 0, max = 20)
  public int handDrinkingThirst = 1;
  @RangeConstraint(min = 0.0D, max = Double.MAX_VALUE)
  public double handDrinkingHydration = 0.1D;
}

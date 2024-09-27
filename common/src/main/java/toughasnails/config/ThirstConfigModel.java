package toughasnails.config;

import blue.endless.jankson.Comment;
import io.wispforest.owo.config.Option.SyncMode;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.Sync;

@Sync(SyncMode.OVERRIDE_CLIENT)
@Config(name = "ToughAsNails_thirst", wrapperName = "ThirstConfig")
public class ThirstConfigModel {
  @Comment("Enable or disable thirst.")
  public boolean enableThirst = true;
  @Comment("Enable or disable hand drinking.")
  public boolean enableHandDrinking = true;
  @Comment("Remove source blocks when filling the canteen.")
  public boolean removeSourceBlocks = false;
  @Comment("Prevent sprinting when thirsty.")
  public boolean thirstPreventSprint = true;
  @Comment("Prevent health regeneration when thirsty.")
  public boolean thirstPreventHealthRegen = true;
  @Comment("The threshold at which exhaustion causes a reduction in hydration and the thirst bar.")
  @RangeConstraint(min = 0.0D, max = Double.MAX_VALUE)
  public double thirstExhaustionThreshold = 8.0D;
  @Comment("Thirst restored from drinking with hands.")
  @RangeConstraint(min = 0, max = 20)
  public int handDrinkingThirst = 1;
  @Comment("Hydration restored from drinking with hands.")
  @RangeConstraint(min = 0.0D, max = Double.MAX_VALUE)
  public double handDrinkingHydration = 0.1D;
}

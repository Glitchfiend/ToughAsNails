package toughasnails.config;

import blue.endless.jankson.Comment;
import io.wispforest.owo.config.Option.SyncMode;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Sync;

@Sync(SyncMode.NONE)
@Config(name = "ToughAsNails_clientconfig", wrapperName = "ClientConfig")
public class ClientConfigModel {
  // Gui
  @Comment("The offset of the left of the thirst overlay from its default position.")
  public int thirstLeftOffset = 0;
  @Comment("The offset of the top of the thirst overlay from its default position.")
  public int thirstTopOffset = 0;
  @Comment("The offset of the left of the temperature overlay from its default position.")
  public int temperatureLeftOffset = 0;
  @Comment("The offset of the top of the temperature overlay from its default position.")
  public int temperatureTopOffset = 0;
}

package toughasnails.config;

import io.wispforest.owo.config.Option.SyncMode;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Sync;
import toughasnails.api.TANAPI;

@Sync(SyncMode.NONE)
@Modmenu(modId = TANAPI.MOD_ID)
@Config(name = "TAN_clientconfig", wrapperName = "ClientConfig")
public class ClientConfigModel {
  // Gui
  public int thirstLeftOffset = 0;
  public int thirstTopOffset = 0;
  public int temperatureLeftOffset = 0;
  public int temperatureTopOffset = 0;
}

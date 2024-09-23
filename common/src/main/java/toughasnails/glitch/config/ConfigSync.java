/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.config;

import toughasnails.glitch.network.SyncConfigPacket;
import toughasnails.glitch.util.Environment;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ConfigSync
{
  private static Map<String, Config> configs = new HashMap<>();

  public static void register(Config config)
  {
    String relative = Environment.getConfigPath().relativize(config.getPath()).toString();
    configs.put(relative, config);
  }

  public static Stream<SyncConfigPacket> createPackets()
  {
    return configs.entrySet().stream().map(e -> {
      var config = e.getValue();

      // Reload the config from the filesystem, but do not save it
      config.read();
      config.load();

      return new SyncConfigPacket(e.getKey(), e.getValue().encode().getBytes(StandardCharsets.UTF_8));
    });
  }

  public static void reload(String path, String toml)
  {
    var config = configs.get(path);
    config.parse(toml);
    config.load();
  }
}

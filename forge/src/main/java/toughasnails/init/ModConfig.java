/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import toughasnails.config.ClientConfig;
import toughasnails.config.ServerConfig;
import toughasnails.config.TemperatureConfig;
import toughasnails.config.ThirstConfig;
import toughasnails.core.ToughAsNailsForge;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig
{
    public static void init()
    {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "toughasnails");

        try
        {
            Files.createDirectory(modConfigPath);
        }
        catch (FileAlreadyExistsException e) {}
        catch (IOException e)
        {
            ToughAsNailsForge.LOGGER.error("Failed to create toughasnails config directory", e);
        }

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ThirstConfig.SPEC, "toughasnails/thirst.toml");
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, TemperatureConfig.SPEC, "toughasnails/temperature.toml");
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ClientConfig.SPEC, "toughasnails/client.toml");
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ServerConfig.SPEC, "toughasnails-server.toml");
    }
}

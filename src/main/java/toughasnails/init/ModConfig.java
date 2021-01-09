/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import toughasnails.config.ServerConfig;
import toughasnails.config.ThirstConfig;
import toughasnails.core.ToughAsNails;

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
            ToughAsNails.logger.error("Failed to create toughasnails config directory", e);
        }

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ThirstConfig.SPEC, "toughasnails/thirst.toml");
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ServerConfig.SPEC, "toughasnails_server.toml");
    }
}

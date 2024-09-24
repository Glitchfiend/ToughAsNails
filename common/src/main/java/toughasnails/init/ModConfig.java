/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.config.ConfigSync;
import glitchcore.util.Environment;
import toughasnails.config.ClientConfig;
import toughasnails.config.TemperatureConfig;
import toughasnails.config.ThirstConfig;

public class ModConfig
{
    public static ThirstConfig thirst;
    public static TemperatureConfig temperature;
    public static ClientConfig client;

    public static void init()
    {
        thirst = new ThirstConfig();
        temperature = new TemperatureConfig();

        ConfigSync.register(thirst);
        ConfigSync.register(temperature);

        if (Environment.isClient())
        {
            client = new ClientConfig();
        }
    }
}

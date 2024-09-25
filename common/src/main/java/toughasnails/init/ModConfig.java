/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

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
        thirst = ThirstConfig.createAndLoad();
        temperature = TemperatureConfig.createAndLoad();

        if (Environment.isClient())
        {
            client = ClientConfig.createAndLoad();
        }
    }
}

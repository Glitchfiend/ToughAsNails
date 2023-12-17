/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import glitchcore.config.Config;
import glitchcore.util.Environment;
import toughasnails.api.TANAPI;

public class ClientConfig extends Config
{
    public int thirstLeftOffset;
    public int thirstTopOffset;
    public int temperatureLeftOffset;
    public int temperatureTopOffset;

    public ClientConfig()
    {
        super(Environment.getConfigPath().resolve(TANAPI.MOD_ID + "/client.toml"));
    }

    @Override
    public void read()
    {
        thirstLeftOffset = addNumber("gui.thirst_left_offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The offset of the left of the thirst overlay from its default position.");
        thirstTopOffset = addNumber("gui.thirst_top_offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The offset of the top of the thirst overlay from its default position.");
        temperatureLeftOffset = addNumber("gui.temperature_left_offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The offset of the left of the temperature overlay from its default position.");
        temperatureTopOffset = addNumber("gui.temperature_top_offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The offset of the top of the temperature overlay from its default position.");
    }
}

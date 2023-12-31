/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import glitchcore.config.Config;
import glitchcore.util.Environment;
import toughasnails.api.TANAPI;

import java.nio.file.Path;

public class ThirstConfig extends Config
{
    public boolean enableThirst;
    public boolean enableHandDrinking;
    public boolean thirstPreventSprint;
    public double thirstExhaustionThreshold;
    public int handDrinkingThirst;
    public double handDrinkingHydration;

    public ThirstConfig()
    {
        super(Environment.getConfigPath().resolve(TANAPI.MOD_ID + "/thirst.toml"));
    }

    @Override
    public void load()
    {
        // Toggles
        enableThirst = add("toggles.enable_thirst", true, "Enable or disable thirst.");
        enableHandDrinking = add("toggles.enable_hand_drinking", false, "Enable or disable hand drinking.");
        thirstPreventSprint = add("toggles.thirst_prevent_sprint", true, "Prevent sprinting when thirsty.");

        // General options
        thirstExhaustionThreshold = addNumber("general.exhaustion_threshold", 8.0D, 0.0D, Double.MAX_VALUE, "The threshold at which exhaustion causes a reduction in hydration and the thirst bar.");

        // Drink options
        handDrinkingThirst = addNumber("drink_options.hand_drinking_thirst", 1, 0, 20, "Thirst restored from drinking with hands.");
        handDrinkingHydration = addNumber("drink_options.hand_drinking_hydration", 0.1D, 0.0D, Double.MAX_VALUE, "Hydration restored from drinking with hands.");
    }
}

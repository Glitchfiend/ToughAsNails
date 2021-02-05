/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue enableThirst;

    static
    {
        BUILDER.push("toggles");
        enableThirst = BUILDER.comment("Enable or disable thirst.").define("enable_thirst", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}

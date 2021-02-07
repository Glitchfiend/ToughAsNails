/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue thirstLeftOffset;
    public static ForgeConfigSpec.IntValue thirstTopOffset;

    static
    {
        BUILDER.push("gui");
        thirstLeftOffset = BUILDER.comment("The offset of the left of the thirst overlay from its default position.").defineInRange("thirst_left_offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        thirstTopOffset = BUILDER.comment("The offset of the top of the thirst overlay from its default position.").defineInRange("thirst_top_offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}

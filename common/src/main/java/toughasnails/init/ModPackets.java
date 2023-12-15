/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import toughasnails.api.TANAPI;
import toughasnails.network.UpdateTemperaturePacket;
import toughasnails.network.UpdateThirstPacket;

public class ModPackets
{
    private static final ResourceLocation CHANNEL = new ResourceLocation(TANAPI.MOD_ID, "main");
    public static final PacketHandler HANDLER = new PacketHandler(CHANNEL);
    public static void init()
    {
        HANDLER.register(new UpdateTemperaturePacket());
        HANDLER.register(new UpdateThirstPacket());
    }
}

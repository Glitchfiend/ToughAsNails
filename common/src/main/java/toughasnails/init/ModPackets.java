/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.minecraft.resources.Identifier;
import toughasnails.api.TANAPI;
import toughasnails.core.ToughAsNails;
import toughasnails.network.DrinkInWorldPacket;
import toughasnails.network.UpdateTemperaturePacket;
import toughasnails.network.UpdateThirstPacket;

public class ModPackets
{
    private static final Identifier CHANNEL = Identifier.fromNamespaceAndPath(TANAPI.MOD_ID, "main");
    public static final PacketHandler HANDLER = new PacketHandler(CHANNEL);
    public static void init()
    {
        register("update_temperature", new UpdateTemperaturePacket());
        register("update_thirst", new UpdateThirstPacket());
        register("drink_in_world", new DrinkInWorldPacket());
    }

    public static void register(String name, CustomPacket<?> packet)
    {
        HANDLER.register(Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, name), packet);
    }
}

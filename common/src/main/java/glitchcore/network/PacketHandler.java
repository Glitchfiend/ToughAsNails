/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class PacketHandler
{
    private final ResourceLocation channelName;

    public PacketHandler(ResourceLocation channelName)
    {
        this.channelName = channelName;
        this.init();
    }

    public void register(CustomPacket<?> packet) { throw new UnsupportedOperationException(); }

    public <T> void sendToPlayer(T data, ServerPlayer player) { throw new UnsupportedOperationException(); }

    public <T> void sendToServer(T data) { throw new UnsupportedOperationException(); }

    private void init() {}
}

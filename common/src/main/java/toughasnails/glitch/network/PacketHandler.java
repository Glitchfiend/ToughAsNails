/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

public final class PacketHandler
{
  private final ResourceLocation channelName;

  public PacketHandler(ResourceLocation channelName)
  {
    this.channelName = channelName;
    this.init();
  }

  public void register(ResourceLocation name, CustomPacket<?> packet) { throw new UnsupportedOperationException(); }

  public <T extends CustomPacket<T>> void sendToPlayer(T data, ServerPlayer player) { throw new UnsupportedOperationException(); }

  public <T extends CustomPacket<T>> void sendToAll(T data, MinecraftServer server) { throw new UnsupportedOperationException(); }

  public <T extends CustomPacket<T>> void sendToHandler(T data, ServerConfigurationPacketListenerImpl handler) { throw new UnsupportedOperationException(); }

  public <T extends CustomPacket<T>> void sendToServer(T data) { throw new UnsupportedOperationException(); }

  private void init() {}
}

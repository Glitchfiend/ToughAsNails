/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.fabric.mixin;

import toughasnails.fabric.network.FabricPacketWrapper;
import toughasnails.fabric.network.IFabricPacketHandler;
import toughasnails.glitch.network.CustomPacket;
import toughasnails.glitch.network.PacketHandler;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jodah.typetools.TypeResolver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.*;

import java.util.HashMap;
import java.util.Map;

// Priority = 0 to facilitate overriding
@Mixin(value = PacketHandler.class, remap = false, priority = 0)
public abstract class MixinPacketHandler implements IFabricPacketHandler
{
  @Shadow
  @Final
  private ResourceLocation channelName;

  @Unique
  private Map<Class<?>, FabricPacketWrapper> wrappers = new HashMap<>();


  @Overwrite
  public <T extends CustomPacket<T>> void register(ResourceLocation name, CustomPacket<T> packet)
  {
    wrappers.put(getPacketDataType(packet), createPacketWrapper(name, packet));
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToPlayer(T packet, ServerPlayer player)
  {
    FabricPacket fPacket = createFabricPacket((CustomPacket)packet);
    switch (packet.getPhase())
    {
      case PLAY -> ServerPlayNetworking.send(player, fPacket);
      default -> throw new UnsupportedOperationException("Attempted to send packet with unsupported phase " + packet.getPhase());
    }
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToAll(T packet, MinecraftServer server)
  {
    FabricPacket fPacket = createFabricPacket((CustomPacket)packet);
    switch (packet.getPhase())
    {
      case PLAY -> server.getPlayerList().broadcastAll(ServerPlayNetworking.createS2CPacket(fPacket));
      default -> throw new UnsupportedOperationException("Attempted to send packet with unsupported phase " + packet.getPhase());
    }
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToHandler(T packet, ServerConfigurationPacketListenerImpl handler)
  {
    FabricPacket fPacket = createFabricPacket((CustomPacket)packet);
    switch (packet.getPhase())
    {
      case CONFIGURATION -> ServerConfigurationNetworking.send(handler, fPacket);
      default -> throw new UnsupportedOperationException("Attempted to send packet with unsupported phase " + packet.getPhase());
    }
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToServer(T packet)
  {
    throw new UnsupportedOperationException("Attempted to call sendToServer from server");
  }

  @Overwrite
  private void init()
  {
  }

  @Override
  public <T extends CustomPacket<T>> FabricPacket createFabricPacket(T packet)
  {
    var dataType = getPacketDataType(packet);

    if (!this.wrappers.containsKey(dataType))
      throw new RuntimeException("Unregistered packet of type " + dataType);

    return this.wrappers.get(dataType).createPacket(packet);
  }

  private static <T extends CustomPacket<T>> Class<?> getPacketDataType(CustomPacket<T> packet)
  {
    final Class<T> dataType = (Class<T>) TypeResolver.resolveRawArgument(CustomPacket.class, packet.getClass());

    if ((Class<?>)dataType == TypeResolver.Unknown.class)
    {
      throw new IllegalStateException("Failed to resolve packet data type: " + packet);
    }

    return dataType;
  }
}
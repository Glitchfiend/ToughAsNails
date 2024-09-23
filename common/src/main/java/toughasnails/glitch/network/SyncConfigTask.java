/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.network;

import toughasnails.core.ToughAsNails;
import toughasnails.glitch.config.ConfigSync;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

import java.util.function.Consumer;

public class SyncConfigTask implements ConfigurationTask
{
  private static final Type TYPE = new Type("toughasnails:sync_config");
  private final ServerConfigurationPacketListenerImpl handler;
  private final Consumer<Type> finish;

  public SyncConfigTask(ServerConfigurationPacketListenerImpl handler, Consumer<Type> finish)
  {
    this.handler = handler;
    this.finish = finish;
  }

  @Override
  public void start(Consumer<Packet<?>> send)
  {
    ConfigSync.createPackets().forEach(p -> ToughAsNails.PACKET_HANDLER.sendToHandler(p, this.handler));
    this.finish.accept(TYPE);
  }

  @Override
  public Type type()
  {
    return TYPE;
  }
}

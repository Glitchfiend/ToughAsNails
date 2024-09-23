/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.network;

import toughasnails.glitch.config.ConfigSync;
import net.minecraft.network.FriendlyByteBuf;

import java.nio.charset.StandardCharsets;

public class SyncConfigPacket implements CustomPacket<SyncConfigPacket>
{
  private String path;
  private byte[] data;

  public SyncConfigPacket(String path, byte[] data)
  {
    this.path = path;
    this.data = data;
  }

  public SyncConfigPacket() {}

  @Override
  public void encode(FriendlyByteBuf buf)
  {
    buf.writeUtf(this.path);
    buf.writeByteArray(this.data);
  }

  @Override
  public SyncConfigPacket decode(FriendlyByteBuf buf)
  {
    return new SyncConfigPacket(buf.readUtf(), buf.readByteArray());
  }

  @Override
  public void handle(SyncConfigPacket data, Context context)
  {
    if (context.isServerSide())
      return;

    ConfigSync.reload(data.path, new String(data.data, StandardCharsets.UTF_8));
  }

  @Override
  public Phase getPhase()
  {
    return Phase.CONFIGURATION;
  }
}

/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin;

import toughasnails.glitch.network.SyncConfigTask;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class MixinServerConfigurationPacketListenerImpl extends ServerCommonPacketListenerImpl implements TickablePacketListener, ServerConfigurationPacketListener
{
  @Shadow @Final private Queue<ConfigurationTask> configurationTasks;

  @Shadow
  public abstract void finishCurrentTask(ConfigurationTask.Type type);

  public MixinServerConfigurationPacketListenerImpl(MinecraftServer server, Connection connection, CommonListenerCookie $$2) {
    super(server, connection, $$2);
  }

  @Inject(method="startConfiguration", at=@At("HEAD"))
  public void onStartConfiguration(CallbackInfo ci)
  {
    this.configurationTasks.add(new SyncConfigTask((ServerConfigurationPacketListenerImpl)(Object)this, this::finishCurrentTask));
  }
}
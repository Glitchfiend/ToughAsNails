/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.mixin.glitchcore.impl;

import com.google.common.base.Preconditions;
import glitchcore.fabric.network.FabricPacketWrapper;
import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jodah.typetools.TypeResolver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler
{
    @Shadow
    @Final
    private ResourceLocation channelName;

    private Map<Class<?>, FabricPacketWrapper> wrappers = new HashMap<>();


    @Overwrite
    public <T extends CustomPacket<T>> void register(CustomPacket<T> packet)
    {
        var dataType = getPacketDataType(packet);
        wrappers.put(dataType, new FabricPacketWrapper(new ResourceLocation(this.channelName.getNamespace(), String.valueOf(dataType.hashCode())), packet));
    }

    @Overwrite
    public <T> void sendToPlayer(T packet, ServerPlayer player)
    {
        ServerPlayNetworking.send(player, createFabricPacket((CustomPacket)packet));
    }

    @Overwrite
    public <T> void sendToServer(T packet)
    {
        ClientPlayNetworking.send(createFabricPacket((CustomPacket)packet));
    }

    @Overwrite
    private void init()
    {
    }

    private <T extends CustomPacket<T>> FabricPacket createFabricPacket(T packet)
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

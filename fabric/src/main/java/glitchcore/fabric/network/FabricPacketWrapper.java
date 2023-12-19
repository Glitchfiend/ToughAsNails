/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricPacketWrapper<T extends CustomPacket<T>>
{
    private final ResourceLocation channel;
    private final CustomPacket<T> packet;
    private final PacketType<?> fabricPacketType;

    public FabricPacketWrapper(ResourceLocation channel, CustomPacket<T> packet)
    {
        this.channel = channel;
        this.packet = packet;
        this.fabricPacketType = PacketType.create(this.channel, Impl::new);

        // Register for handling on both the client and server
        ServerPlayNetworking.registerGlobalReceiver(this.fabricPacketType, new ServerPlayNetworking.PlayPacketHandler()
        {
            @Override
            public void receive(FabricPacket packet, ServerPlayer player, PacketSender responseSender)
            {
                FabricPacketWrapper.this.packet.handle(((Impl)packet).data, new CustomPacket.Context()
                {
                    @Override
                    public boolean isClientSide()
                    {
                        return false;
                    }

                    @Override
                    public ServerPlayer getSender()
                    {
                        return player;
                    }
                });
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(this.fabricPacketType, new ClientPlayNetworking.PlayPacketHandler() {
            @Override
            public void receive(FabricPacket packet, LocalPlayer player, PacketSender responseSender)
            {
                FabricPacketWrapper.this.packet.handle(((Impl)packet).data, new CustomPacket.Context()
                {
                    @Override
                    public boolean isClientSide()
                    {
                        return true;
                    }

                    @Override
                    public ServerPlayer getSender()
                    {
                        return null;
                    }
                });
            }
        });
    }

    public FabricPacket createPacket(T data)
    {
        return new Impl(data);
    }

    private class Impl implements FabricPacket
    {
        private final T data;

        private Impl(T data)
        {
            this.data = data;
        }

        private Impl(FriendlyByteBuf buf)
        {
            this.data = FabricPacketWrapper.this.packet.decode(buf);
        }

        @Override
        public void write(FriendlyByteBuf buf)
        {
            FabricPacketWrapper.this.packet.encode(buf);
        }

        @Override
        public PacketType<?> getType()
        {
            return FabricPacketWrapper.this.fabricPacketType;
        }
    }
}

/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.network;

import glitchcore.network.CustomPacket;
import net.jodah.typetools.TypeResolver;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;

public final class PacketHandlerImpl
{
    public static <T> void register(SimpleChannel channel, CustomPacket<T> packet)
    {
        final Class<T> dataType = (Class<T>)TypeResolver.resolveRawArgument(CustomPacket.class, packet.getClass());

        if (dataType == TypeResolver.Unknown.class)
        {
            throw new IllegalStateException("Failed to resolve consumer event type: " + packet);
        }
        
        channel.messageBuilder(dataType).encoder(packet::encode).decoder(packet::decode).consumerMainThread((data, forgeContext) ->
        {
            forgeContext.enqueueWork(() ->
            {
                packet.handle(data, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return forgeContext.isClientSide();
                    }

                    @Override
                    public boolean isServerSide() {
                        return forgeContext.isServerSide();
                    }
                });
            });
            forgeContext.setPacketHandled(true);
        }).add();
    }

    public static <T> void sendToPlayer(SimpleChannel channel, T data, ServerPlayer player)
    {
        channel.send(data, PacketDistributor.PLAYER.with(player));
    }

    public static <T> void sendToServer(SimpleChannel channel, T data)
    {
        channel.send(data, PacketDistributor.SERVER.noArg());
    }
}

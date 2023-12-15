/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import toughasnails.core.ToughAsNailsForge;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketHandler
{
    public static final int PROTOCOL_VERSION = 0;
    public static final SimpleChannel HANDLER = ChannelBuilder
            .named(new ResourceLocation(ToughAsNailsForge.MOD_ID, "main_channel"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();

    public static void init()
    {
        registerMessage(MessageDrinkInWorld.class, MessageDrinkInWorld::encode, MessageDrinkInWorld::decode, MessageDrinkInWorld.Handler::handle);
    }

    private static <T> void registerMessage(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, CustomPayloadEvent.Context> consumer)
    {
        HANDLER.messageBuilder(type).encoder(encoder).decoder(decoder).consumerMainThread(consumer).add();
    }
}

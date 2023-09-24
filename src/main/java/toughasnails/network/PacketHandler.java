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
import toughasnails.core.ToughAsNails;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketHandler
{
    public static final int PROTOCOL_VERSION = 0;
    public static final SimpleChannel HANDLER = ChannelBuilder
            .named(new ResourceLocation(ToughAsNails.MOD_ID, "main_channel"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();
    private static int nextFreeIndex;

    public static void init()
    {
        registerMessage(MessageUpdateThirst.class, MessageUpdateThirst::encode, MessageUpdateThirst::decode, MessageUpdateThirst.Handler::handle);
        registerMessage(MessageDrinkInWorld.class, MessageDrinkInWorld::encode, MessageDrinkInWorld::decode, MessageDrinkInWorld.Handler::handle);
        registerMessage(MessageUpdateTemperature.class, MessageUpdateTemperature::encode, MessageUpdateTemperature::decode, MessageUpdateTemperature.Handler::handle);
    }

    private static <T> void registerMessage(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, CustomPayloadEvent.Context> consumer)
    {
        HANDLER.messageBuilder(type).encoder(encoder).decoder(decoder).consumerMainThread(consumer).add();
    }
}

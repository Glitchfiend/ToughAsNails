package toughasnails.mixin.glitchcore.impl;

import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.jodah.typetools.TypeResolver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler
{
    @Shadow
    @Final
    private ResourceLocation channelName;

    private SimpleChannel channel;

    @Overwrite
    public <T extends CustomPacket<T>> void register(CustomPacket<T> packet)
    {
        final Class<T> dataType = (Class<T>) TypeResolver.resolveRawArgument(CustomPacket.class, packet.getClass());

        if ((Class<?>)dataType == TypeResolver.Unknown.class)
        {
            throw new IllegalStateException("Failed to resolve packet data type: " + packet);
        }

        this.channel.messageBuilder(dataType).encoder(CustomPacket::encode).decoder(packet::decode).consumerMainThread((data, forgeContext) ->
        {
            forgeContext.enqueueWork(() ->
            {
                packet.handle(data, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return forgeContext.isClientSide();
                    }

                    @Override
                    public ServerPlayer getSender()
                    {
                        return forgeContext.getSender();
                    }
                });
            });
            forgeContext.setPacketHandled(true);
        }).add();
    }

    @Overwrite
    public <T> void sendToPlayer(T data, ServerPlayer player)
    {
        channel.send(data, PacketDistributor.PLAYER.with(player));
    }

    @Overwrite
    public <T> void sendToServer(T data)
    {
        channel.send(data, PacketDistributor.SERVER.noArg());
    }

    @Overwrite
    private void init()
    {
        this.channel = ChannelBuilder.named(this.channelName).simpleChannel();
    }
}

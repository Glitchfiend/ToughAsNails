package toughasnails.mixin.glitchcore.impl;

import glitchcore.forge.network.PacketHandlerImpl;
import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
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
    public void register(CustomPacket<?> packet)
    {
        PacketHandlerImpl.register(this.channel, packet);
    }

    @Overwrite
    public <T> void sendToPlayer(T data, ServerPlayer player)
    {
        PacketHandlerImpl.sendToPlayer(this.channel, data, player);
    }

    @Overwrite
    public <T> void sendToServer(T data)
    {
        PacketHandlerImpl.sendToServer(this.channel, data);
    }

    @Overwrite
    private void init()
    {
        this.channel = ChannelBuilder.named(this.channelName).simpleChannel();
    }
}

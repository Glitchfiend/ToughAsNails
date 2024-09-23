package toughasnails.forge.mixin;

import toughasnails.glitch.network.CustomPacket;
import toughasnails.glitch.network.PacketHandler;
import net.jodah.typetools.TypeResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.spongepowered.asm.mixin.*;

import java.util.Optional;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler
{
  @Unique
  private static final PacketDistributor<ServerGamePacketListenerImpl> HANDLER_DISTRIBUTOR = new PacketDistributor<>((distributor, handler) -> handler::send);

  @Shadow
  @Final
  private ResourceLocation channelName;

  @Unique
  private SimpleChannel channel;

  @Overwrite
  public <T extends CustomPacket<T>> void register(ResourceLocation name, CustomPacket<T> packet)
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
          public Optional<Player> getPlayer()
          {
            return Optional.ofNullable((Player)forgeContext.getSender()).or(() -> isClientSide() ? Optional.ofNullable(Minecraft.getInstance().player) : Optional.empty());
          }
        });
      });
      forgeContext.setPacketHandled(true);
    }).add();
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToPlayer(T data, ServerPlayer player)
  {
    channel.send(data, PacketDistributor.PLAYER.with(player));
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToAll(T packet, MinecraftServer server)
  {
    channel.send(packet, PacketDistributor.ALL.noArg());
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToHandler(T packet, ServerConfigurationPacketListenerImpl handler)
  {
    var connection = handler.getConnection();
    channel.send(packet, connection);
  }

  @Overwrite
  public <T extends CustomPacket<T>> void sendToServer(T data)
  {
    channel.send(data, PacketDistributor.SERVER.noArg());
  }

  @Overwrite
  private void init()
  {
    this.channel = ChannelBuilder.named(this.channelName).simpleChannel();
  }
}
package toughasnails.glitch.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface CustomPacket<T extends CustomPacket<T>>
{
  void encode(FriendlyByteBuf buf);

  T decode(FriendlyByteBuf buf);

  void handle(T data, Context context);

  default Phase getPhase()
  {
    return Phase.PLAY;
  }

  interface Context
  {
    boolean isClientSide();
    default boolean isServerSide()
    {
      return !isClientSide();
    }
    Optional<Player> getPlayer();
  }

  enum Phase
  {
    PLAY,
    CONFIGURATION
  }
}

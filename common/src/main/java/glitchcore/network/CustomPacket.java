package glitchcore.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public interface CustomPacket<T extends CustomPacket<T>>
{
    void encode(FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    void handle(T data, Context context);

    interface Context
    {
        boolean isClientSide();
        boolean isServerSide();
        ServerPlayer getSender();
    }
}

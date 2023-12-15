package glitchcore.network;

import net.minecraft.network.FriendlyByteBuf;

public interface CustomPacket<T>
{
    void encode(T data, FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    void handle(T data, Context context);

    interface Context
    {
        boolean isClientSide();
        boolean isServerSide();
    }
}

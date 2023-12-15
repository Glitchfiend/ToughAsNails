/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import glitchcore.network.CustomPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

public class UpdateThirstPacket implements CustomPacket<UpdateThirstPacket.Data>
{
    @Override
    public void encode(Data data, FriendlyByteBuf buf)
    {
        buf.writeInt(data.thirstLevel());
        buf.writeFloat(data.hydrationLevel());
    }

    @Override
    public Data decode(FriendlyByteBuf buf)
    {
        return new Data(buf.readInt(), buf.readFloat());
    }

    @Override
    public void handle(Data data, Context context)
    {
        if (context.isServerSide())
            return;

        Player player = Minecraft.getInstance().player;
        IThirst thirst = ThirstHelper.getThirst(player);

        thirst.setThirst(data.thirstLevel());
        thirst.setHydration(data.hydrationLevel());
    }

    public record Data(int thirstLevel, float hydrationLevel) {}
}

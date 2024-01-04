/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import glitchcore.network.CustomPacket;
import net.minecraft.network.FriendlyByteBuf;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

public class UpdateThirstPacket implements CustomPacket<UpdateThirstPacket>
{
    private int thirstLevel;
    private float hydrationLevel;

    public UpdateThirstPacket(int thirstLevel, float hydrationLevel)
    {
        this.thirstLevel = thirstLevel;
        this.hydrationLevel = hydrationLevel;
    }

    public UpdateThirstPacket() {}

    @Override
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeInt(this.thirstLevel);
        buf.writeFloat(this.hydrationLevel);
    }

    @Override
    public UpdateThirstPacket decode(FriendlyByteBuf buf)
    {
        return new UpdateThirstPacket(buf.readInt(), buf.readFloat());
    }

    @Override
    public void handle(UpdateThirstPacket packet, Context context)
    {
        if (context.isServerSide())
            return;

        context.getPlayer().ifPresent(player -> {
            IThirst thirst = ThirstHelper.getThirst(player);

            thirst.setThirst(packet.thirstLevel);
            thirst.setHydration(packet.hydrationLevel);
        });
    }
}

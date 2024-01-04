/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import glitchcore.network.CustomPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class UpdateTemperaturePacket implements CustomPacket<UpdateTemperaturePacket>
{
    private TemperatureLevel temperatureLevel;
    private int hyperthermiaTicks;

    public UpdateTemperaturePacket(TemperatureLevel temperatureLevel, int hyperthermiaTicks)
    {
        this.temperatureLevel = temperatureLevel;
        this.hyperthermiaTicks = hyperthermiaTicks;
    }

    public UpdateTemperaturePacket() {}

    @Override
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeEnum(this.temperatureLevel);
        buf.writeInt(this.hyperthermiaTicks);
    }

    @Override
    public UpdateTemperaturePacket decode(FriendlyByteBuf buf)
    {
        return new UpdateTemperaturePacket(buf.readEnum(TemperatureLevel.class), buf.readInt());
    }

    @Override
    public void handle(UpdateTemperaturePacket packet, Context context)
    {
        if (context.isServerSide())
            return;

        context.getPlayer().ifPresent(player -> {
            ITemperature temperature = TemperatureHelper.getTemperatureData(player);

            temperature.setLevel(packet.temperatureLevel);
            temperature.setHyperthermiaTicks(packet.hyperthermiaTicks);
        });
    }
}

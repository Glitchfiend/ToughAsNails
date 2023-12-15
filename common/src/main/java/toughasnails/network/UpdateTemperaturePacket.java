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
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

public class UpdateTemperaturePacket implements CustomPacket<UpdateTemperaturePacket.Data>
{
    @Override
    public void encode(Data data, FriendlyByteBuf buf)
    {
        buf.writeEnum(data.temperatureLevel());
        buf.writeInt(data.hyperthermiaTicks());
    }

    @Override
    public Data decode(FriendlyByteBuf buf)
    {
        return new Data(buf.readEnum(TemperatureLevel.class), buf.readInt());
    }

    @Override
    public void handle(Data data, Context context)
    {
        if (context.isServerSide())
            return;

        Player player = Minecraft.getInstance().player;
        ITemperature temperature = TemperatureHelper.getTemperatureData(player);

        temperature.setLevel(data.temperatureLevel());
        temperature.setHyperthermiaTicks(data.hyperthermiaTicks());
    }

    public record Data(TemperatureLevel temperatureLevel, int hyperthermiaTicks) {}
}

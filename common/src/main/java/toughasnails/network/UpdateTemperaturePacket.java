/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import glitchcore.network.CustomPacket;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class UpdateTemperaturePacket implements CustomPacket<UpdateTemperaturePacket>
{
    private TemperatureLevel temperatureLevel;
    private int hyperthermiaTicks;
    private Set<BlockPos> nearbyThermoregulators;

    public UpdateTemperaturePacket(TemperatureLevel temperatureLevel, int hyperthermiaTicks, Set<BlockPos> nearbyThermoregulators)
    {
        this.temperatureLevel = temperatureLevel;
        this.hyperthermiaTicks = hyperthermiaTicks;
        this.nearbyThermoregulators = nearbyThermoregulators;
    }

    public UpdateTemperaturePacket() {}

    @Override
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeEnum(this.temperatureLevel);
        buf.writeInt(this.hyperthermiaTicks);
        buf.writeCollection(this.nearbyThermoregulators, FriendlyByteBuf::writeBlockPos);
    }

    @Override
    public UpdateTemperaturePacket decode(FriendlyByteBuf buf)
    {
        return new UpdateTemperaturePacket(buf.readEnum(TemperatureLevel.class), buf.readInt(), buf.readCollection(HashSet::new, FriendlyByteBuf::readBlockPos));
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
            temperature.setNearbyThermoregulators(packet.nearbyThermoregulators);
        });
    }
}

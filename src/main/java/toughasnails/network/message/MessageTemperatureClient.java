package toughasnails.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.api.TANCapabilities;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MessageTemperatureClient implements IMessage, IMessageHandler<MessageTemperatureClient, IMessage>
{
    public int temperatureTimer;
    public int changeTicks;
    public int targetTemperature;
    
    public Map<String, IModifierMonitor.Context> modifiers = new LinkedHashMap();

    public MessageTemperatureClient() {}

    public MessageTemperatureClient(int temperatureTimer, int changeTicks, int targetTemperature, Map<String, IModifierMonitor.Context> modifiers)
    {
        this.temperatureTimer = temperatureTimer;
        this.changeTicks = changeTicks;
        this.targetTemperature = targetTemperature;
        this.modifiers = modifiers;
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        PacketBuffer packetBuffer = new PacketBuffer(buf);

        this.temperatureTimer = packetBuffer.readInt();
        this.changeTicks = packetBuffer.readInt();
        this.targetTemperature = packetBuffer.readInt();

        int size = packetBuffer.readInt();

        for (int i = 0; i < size; i++)
        {
            String modifierId = ByteBufUtils.readUTF8String(buf);
            String description = ByteBufUtils.readUTF8String(buf);
            Temperature startTemp = new Temperature(buf.readInt());
            Temperature endTemp = new Temperature(buf.readInt());

            modifiers.put(modifierId, new IModifierMonitor.Context(modifierId, description, startTemp, endTemp));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        PacketBuffer packetBuffer = new PacketBuffer(buf);

        packetBuffer.writeInt(this.temperatureTimer);
        packetBuffer.writeInt(this.changeTicks);
        packetBuffer.writeInt(this.targetTemperature);

        packetBuffer.writeInt(modifiers.size());

        for (Entry<String, IModifierMonitor.Context> entry : modifiers.entrySet())
        {
            ByteBufUtils.writeUTF8String(buf, entry.getValue().modifierId);
            ByteBufUtils.writeUTF8String(buf, entry.getValue().description);
            packetBuffer.writeInt(entry.getValue().startTemperature.getRawValue());
            packetBuffer.writeInt(entry.getValue().endTemperature.getRawValue());
        }
    }

    @Override
    public IMessage onMessage(MessageTemperatureClient message, MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT)
        {
            EntityPlayerSP player =  Minecraft.getMinecraft().player;

            if (player != null)
            {
                TemperatureHandler temperatureStats = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
                TemperatureDebugger debugger = temperatureStats.debugger;
                
                debugger.temperatureTimer = message.temperatureTimer;
                debugger.changeTicks = message.changeTicks;
                debugger.targetTemperature = message.targetTemperature;
                debugger.modifiers = message.modifiers;
            }
        }
        
        return null;
    }
}

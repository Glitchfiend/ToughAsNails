package toughasnails.network.message;

import io.netty.buffer.ByteBuf;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureDebugger.ModifierType;
import toughasnails.temperature.TemperatureStats;

public class MessageTemperatureDebug implements IMessage, IMessageHandler<MessageTemperatureDebug, IMessage>
{
    public int temperatureTimer;
    public int changeTicks;
    public int targetTemperature;
    
    public Map<Modifier, Integer>[] modifiers = new LinkedHashMap[ModifierType.values().length];
    
    public MessageTemperatureDebug() 
    {
        for (int i = 0; i < ModifierType.values().length; i++)
        {
            modifiers[i] = new LinkedHashMap();
        }
    }
    
    public MessageTemperatureDebug(int temperatureTimer, int changeTicks, int targetTemperature, Map<Modifier, Integer>[] modifiers)
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
        
        for (int mapIdx = 0; mapIdx < modifiers.length; mapIdx++)
        {
            int size = packetBuffer.readInt();

            for (int i = 0; i < size; i++)
            {
                Modifier modifier = (Modifier)packetBuffer.readEnumValue(Modifier.class);
                int value = packetBuffer.readInt();
                
                modifiers[mapIdx].put(modifier, value);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        
        packetBuffer.writeInt(this.temperatureTimer);
        packetBuffer.writeInt(this.changeTicks);
        packetBuffer.writeInt(this.targetTemperature);

        for (Map<Modifier, Integer> modifier : modifiers)
        {
            packetBuffer.writeInt(modifier.size());

            for (Entry<Modifier, Integer> entry : modifier.entrySet())
            {
                packetBuffer.writeEnumValue(entry.getKey());
                packetBuffer.writeInt(entry.getValue());
            }
        }
    }

    @Override
    public IMessage onMessage(MessageTemperatureDebug message, MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT)
        {
            EntityPlayerSP player =  Minecraft.getMinecraft().thePlayer;

            if (player != null)
            {
                TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");
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

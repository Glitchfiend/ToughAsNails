package toughasnails.network.message;

import toughasnails.temperature.TemperatureStats;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateTemperature implements IMessage, IMessageHandler<MessageUpdateTemperature, IMessage>
{
    public int temperatureLevel;
    
    public MessageUpdateTemperature() {}

    public MessageUpdateTemperature(int temperatureLevel)
    {
        this.temperatureLevel = temperatureLevel;
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.temperatureLevel = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.temperatureLevel); 
    }

    @Override
    public IMessage onMessage(MessageUpdateTemperature message, MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT)
        {
            EntityPlayerSP player =  Minecraft.getMinecraft().thePlayer;

            if (player != null)
            {
                TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");

                temperatureStats.setTemperature(message.temperatureLevel);
            }
        }

        return null;
    }
}

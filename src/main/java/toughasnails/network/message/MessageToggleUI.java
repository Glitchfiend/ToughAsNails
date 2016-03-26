package toughasnails.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.api.TANCapabilities;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;

public class MessageToggleUI implements IMessage, IMessageHandler<MessageToggleUI, IMessage>
{
    public boolean showDebugGUI;
    
    public MessageToggleUI() {}
    
    public MessageToggleUI(boolean showDebugGUI)
    {
        this.showDebugGUI = showDebugGUI;
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.showDebugGUI = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.showDebugGUI);
    }
    
    @Override
    public IMessage onMessage(MessageToggleUI message, MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT)
        {
            EntityPlayerSP player =  Minecraft.getMinecraft().thePlayer;

            if (player != null)
            {
                TemperatureHandler temperatureStats = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
                TemperatureDebugger debugger = temperatureStats.debugger;
                
                debugger.setGuiVisible(message.showDebugGUI);
            }
        }
        
        return null;
    }
}

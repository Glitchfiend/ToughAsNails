package toughasnails.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import toughasnails.api.PlayerStat;

public class MessageUpdateStat implements IMessage, IMessageHandler<MessageUpdateStat, IMessage>
{
    public String identifier;
    public NBTTagCompound data;
    
    public MessageUpdateStat() {}
    
    public MessageUpdateStat(String identifier, NBTTagCompound data)
    {
        this.identifier = identifier;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.identifier = ByteBufUtils.readUTF8String(buf);
        this.data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.identifier);
        ByteBufUtils.writeTag(buf, this.data);
    }
    
    @Override
    public IMessage onMessage(MessageUpdateStat message, MessageContext ctx)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        
        if (player != null)
        {
            PlayerStat stat = (PlayerStat)player.getExtendedProperties(message.identifier);
            
            stat.loadNBTData(message.data);
        }
        
        return null;
    }
}

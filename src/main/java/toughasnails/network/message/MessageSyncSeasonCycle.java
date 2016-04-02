/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.handler.season.SeasonHandler;

public class MessageSyncSeasonCycle implements IMessage, IMessageHandler<MessageSyncSeasonCycle, IMessage>
{
    public int seasonCycleTicks;
    
    public MessageSyncSeasonCycle() {}
    
    public MessageSyncSeasonCycle(int seasonCycleTicks)
    {
        this.seasonCycleTicks = seasonCycleTicks;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) 
    {
        this.seasonCycleTicks = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
        buf.writeInt(this.seasonCycleTicks);
    }

    @Override
    public IMessage onMessage(MessageSyncSeasonCycle message, MessageContext ctx)
    {
        if (ctx.side == Side.CLIENT)
        {
            SeasonHandler.clientSeasonCycleTicks = message.seasonCycleTicks;
        }
        
        return null;
    }
}

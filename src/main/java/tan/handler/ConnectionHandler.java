package tan.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import tan.api.utils.TANPlayerStatUtils;
import tan.network.PacketTypeHandler;
import tan.network.packet.PacketSendStats;
import tan.stats.TemperatureStat;
import tan.stats.ThirstStat;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ConnectionHandler implements IConnectionHandler
{
    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
    {
            EntityPlayer entityPlayer = (EntityPlayer)player;
            
            TemperatureStat temperatureStat = TANPlayerStatUtils.getPlayerStat(entityPlayer, TemperatureStat.class);
            ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(entityPlayer, ThirstStat.class);
            
            PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketSendStats(entityPlayer)), (Player)player);
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
    {
        return null;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server,int port, INetworkManager manager)
    {
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
    {
    }

    @Override
    public void connectionClosed(INetworkManager manager)
    {
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
    }
}

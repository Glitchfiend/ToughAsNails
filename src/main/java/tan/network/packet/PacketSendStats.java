package tan.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import tan.api.utils.TANPlayerStatUtils;
import tan.network.PacketTypeHandler;
import tan.stats.TemperatureStat;
import tan.stats.ThirstStat;
import cpw.mods.fml.common.network.Player;

public class PacketSendStats extends PacketTAN
{
    private float temperatureLevel;
    private int temperatureTimer;
    
    private int thirstLevel;
    private float thirstExhaustionLevel;
    private float thirstHydrationLevel;
    private int thirstTimer;
    
    public PacketSendStats()
    {
        super(PacketTypeHandler.sendStats);
    }
    
    public PacketSendStats(EntityPlayer player)
    {
        super(PacketTypeHandler.sendStats);
        
        TemperatureStat temperatureStat = TANPlayerStatUtils.getPlayerStat(player, TemperatureStat.class);
        ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(player, ThirstStat.class);
        
        temperatureLevel = temperatureStat.temperatureLevel;
        temperatureTimer = temperatureStat.temperatureTimer;
        
        thirstLevel = thirstStat.thirstLevel;
        thirstHydrationLevel = thirstStat.thirstHydrationLevel;
        thirstExhaustionLevel = thirstStat.thirstExhaustionLevel;
        thirstTimer = thirstStat.thirstTimer;
    }

    @Override
    public void readData(DataInputStream data) throws IOException 
    {
        temperatureLevel = data.readFloat();
        temperatureTimer = data.readInt();
        
        thirstLevel = data.readInt();
        thirstHydrationLevel = data.readFloat();
        thirstExhaustionLevel = data.readFloat();
        thirstTimer = data.readInt();
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException 
    {
        data.writeFloat(temperatureLevel);
        data.writeInt(temperatureTimer);
        
        data.writeInt(thirstLevel);
        data.writeFloat(thirstHydrationLevel);
        data.writeFloat(thirstExhaustionLevel);
        data.writeInt(thirstTimer);
    }

    @Override
    public void execute(INetworkManager network, Player player) 
    {
        EntityPlayer entityPlayer = (EntityPlayer)player;

        if (entityPlayer.worldObj.isRemote)
        {
            TemperatureStat temperatureStat = TANPlayerStatUtils.getPlayerStat(entityPlayer, TemperatureStat.class);
            ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(entityPlayer, ThirstStat.class);

            temperatureStat.temperatureLevel = temperatureLevel;
            temperatureStat.temperatureTimer = temperatureTimer;

            thirstStat.thirstLevel = thirstLevel;
            thirstStat.thirstHydrationLevel = thirstHydrationLevel;
            thirstStat.thirstExhaustionLevel = thirstExhaustionLevel;
            thirstStat.thirstTimer = thirstTimer;

            TANPlayerStatUtils.setPlayerStat(entityPlayer, temperatureStat);
            TANPlayerStatUtils.setPlayerStat(entityPlayer, thirstStat);
        }
    }
}

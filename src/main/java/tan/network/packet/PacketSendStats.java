package tan.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import tan.api.PlayerStatRegistry;
import tan.network.PacketTypeHandler;
import tan.stats.TemperatureStat;
import tan.stats.ThirstStat;
import cpw.mods.fml.common.network.Player;

public class PacketSendStats extends PacketTAN
{
    float temperature;
    
    private int thirstLevel;
    private float thirstExhaustionLevel;
    private int thirstTimer;
    
    public PacketSendStats()
    {
        super(PacketTypeHandler.sendStats);
    }
    
    public PacketSendStats(NBTTagCompound tanCompound)
    {
        super(PacketTypeHandler.sendStats);
        
        temperature = tanCompound.getFloat(PlayerStatRegistry.getStatName(TemperatureStat.class));
        
        NBTTagCompound thirstCompound = tanCompound.getCompoundTag(PlayerStatRegistry.getStatName(ThirstStat.class));
        
        thirstLevel = thirstCompound.getInteger("thirstLevel");
        thirstExhaustionLevel = thirstCompound.getFloat("thirstExhaustionLevel");
        thirstTimer = thirstCompound.getInteger("thirstTimer");
    }

    @Override
    public void readData(DataInputStream data) throws IOException 
    {
        temperature = data.readFloat();
        
        thirstLevel = data.readInt();
        thirstExhaustionLevel = data.readFloat();
        thirstTimer = data.readInt();
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException 
    {
        data.writeFloat(temperature);
        
        data.writeInt(thirstLevel);
        data.writeFloat(thirstExhaustionLevel);
        data.writeInt(thirstTimer);
    }

    @Override
    public void execute(INetworkManager network, Player player) 
    {
        EntityPlayer entityPlayer = (EntityPlayer)player;
        
        NBTTagCompound tanCompound = entityPlayer.getEntityData().getCompoundTag("ToughAsNails");
        
        tanCompound.setFloat(PlayerStatRegistry.getStatName(TemperatureStat.class), temperature);
        
        NBTTagCompound thirstCompound = new NBTTagCompound();
        
        thirstCompound.setInteger("thirstLevel", thirstLevel);
        thirstCompound.setFloat("thirstExhaustionLevel", thirstExhaustionLevel);
        thirstCompound.setInteger("thirstTimer", thirstTimer);
        
        tanCompound.setCompoundTag(PlayerStatRegistry.getStatName(ThirstStat.class), thirstCompound);
        
        entityPlayer.getEntityData().setCompoundTag("ToughAsNails", tanCompound);
    }
}

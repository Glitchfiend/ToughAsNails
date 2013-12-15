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
    
    public PacketSendStats(NBTTagCompound tanData)
    {
        super(PacketTypeHandler.sendStats);
        
        NBTTagCompound temperatureCompound = tanData.getCompoundTag("temperature");
        
        temperatureLevel = temperatureCompound.getFloat("temperatureLevel");
        temperatureTimer = temperatureCompound.getInteger("temperatureTimer");
        
        NBTTagCompound thirstCompound = tanData.getCompoundTag("thirst");
        
        thirstLevel = thirstCompound.getInteger("thirstLevel");
        thirstHydrationLevel = thirstCompound.getFloat("thirstHydrationLevel");
        thirstExhaustionLevel = thirstCompound.getFloat("thirstExhaustionLevel");
        thirstTimer = thirstCompound.getInteger("thirstTimer");
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
        
        NBTTagCompound tanCompound = entityPlayer.getEntityData().getCompoundTag("ToughAsNails");
        
        NBTTagCompound temperatureCompound = new NBTTagCompound();
        
        temperatureCompound.setFloat("temperatureLevel", temperatureLevel);
        temperatureCompound.setInteger("temperatureTimer", temperatureTimer);
        
        NBTTagCompound thirstCompound = new NBTTagCompound();
        
        thirstCompound.setInteger("thirstLevel", thirstLevel);
        thirstCompound.setFloat("thirstHydrationLevel", thirstHydrationLevel);
        thirstCompound.setFloat("thirstExhaustionLevel", thirstExhaustionLevel);
        thirstCompound.setInteger("thirstTimer", thirstTimer);
        
        tanCompound.setCompoundTag("temperature", temperatureCompound);
        tanCompound.setCompoundTag("thirst", thirstCompound);
        
        entityPlayer.getEntityData().setCompoundTag("ToughAsNails", tanCompound);
    }
}

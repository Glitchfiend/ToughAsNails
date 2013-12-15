package tan.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tan.network.PacketTypeHandler;
import tan.network.packet.PacketSendStats;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public abstract class TANStat
{
    public World world;
    public EntityPlayerMP player;
        
    public abstract void update();
    
    public abstract void readNBT(NBTTagCompound tanData);
    
    public abstract void writeNBT(NBTTagCompound tanData);
    
    public abstract void setDefaults(NBTTagCompound tanData);
    
    public static void updatePlayerData(NBTTagCompound tanData, EntityPlayerMP player)
    {
        player.getEntityData().setCompoundTag("ToughAsNails", tanData);
        PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketSendStats(tanData)), (Player)player);
    }
    
    public void setDefaultCompound(NBTTagCompound tanData, String key, NBTTagCompound compound)
    {
        if (!tanData.hasKey(key))
        {
            tanData.setCompoundTag(key, compound);
            updatePlayerData(tanData, player);
        }
    }
    
    public void setDefaultInt(NBTTagCompound tanData, String key, int value)
    {
        if (!tanData.hasKey(key)) 
        {
            tanData.setInteger(key, value);
            updatePlayerData(tanData, player);
        }
    }
    
    public void setDefaultFloat(NBTTagCompound tanData, String key, float value)
    {
        if (!tanData.hasKey(key)) 
        {
            tanData.setFloat(key, value);
            updatePlayerData(tanData, player);
        }
    }
}

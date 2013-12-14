package tan.api;

import tan.network.PacketTypeHandler;
import tan.network.packet.PacketSendStats;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class TANStat
{
    public World world;
    public EntityPlayerMP player;
        
    public abstract void update();
    
    public abstract void readNBT(NBTTagCompound tanData);
    
    public abstract void writeNBT(NBTTagCompound tanData);

    public abstract String getStatName();
    
    public static void updatePlayerData(NBTTagCompound tanData, EntityPlayerMP player)
    {
        player.getEntityData().setCompoundTag("ToughAsNails", tanData);
        PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketSendStats(tanData)), (Player)player);
    }
}

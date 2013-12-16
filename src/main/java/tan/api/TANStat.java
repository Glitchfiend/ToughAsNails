package tan.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tan.network.PacketTypeHandler;
import tan.network.packet.PacketSendStats;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public abstract class TANStat
{     
    public abstract void update(EntityPlayer player);
    
    public abstract void readNBT(NBTTagCompound tanData);
    
    public abstract void writeNBT(NBTTagCompound tanData);
}

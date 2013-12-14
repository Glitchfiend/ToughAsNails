package tan.handler;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tan.api.TANStat;
import tan.api.PlayerStatRegistry;

public class TANPlayerStatHandler
{
    public static void update(World world, EntityPlayerMP player)
    {
        NBTTagCompound tanData = player.getEntityData().getCompoundTag("ToughAsNails");
        
        for (TANStat stat : PlayerStatRegistry.tanStatList)
        {
            stat.world = world;
            stat.player = player;
            
            stat.readNBT(tanData);
            stat.update();
            stat.writeNBT(tanData);
        }
    }
}

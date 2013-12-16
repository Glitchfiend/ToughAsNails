package tan.api.utils;

import tan.api.TANStat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TANPlayerStatUtils
{
    public static <Stat extends TANStat> Stat getPlayerStat(EntityPlayer player, Class<Stat> clazz)
    {
        Stat stat = null;
        
        try
        {
            stat = clazz.newInstance();
            
            if (!player.getEntityData().hasKey("ToughAsNails")) player.getEntityData().setCompoundTag("ToughAsNails", new NBTTagCompound());
            
            stat.readNBT(player.getEntityData().getCompoundTag("ToughAsNails"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }     

        return stat;
    }

    public static void setPlayerStat(EntityPlayer player, TANStat stat)
    {       
        if (!player.getEntityData().hasKey("ToughAsNails")) player.getEntityData().setCompoundTag("ToughAsNails", new NBTTagCompound());
        
        stat.writeNBT(player.getEntityData().getCompoundTag("ToughAsNails"));
    }
}

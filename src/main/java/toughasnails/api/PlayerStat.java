package toughasnails.api;

import toughasnails.network.message.MessageUpdateStat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PlayerStat implements IExtendedEntityProperties
{
    public final String identifier;
    
    public PlayerStat(String identifier)
    {
        this.identifier = identifier;
    }
    
    @Override
    public final void init(Entity entity, World world)
    {
        this.init((EntityPlayer)entity, world);
    }
    
    public abstract void init(EntityPlayer player, World world);
    public abstract void update(EntityPlayer player, World world);
    public abstract boolean shouldUpdateClient();
    
    public void onSendClientUpdate() {}
    
    public IMessage createUpdateMessage()
    {
        NBTTagCompound data = new NBTTagCompound();
        
        this.saveNBTData(data);

        return new MessageUpdateStat(this.identifier, data);
    }
}

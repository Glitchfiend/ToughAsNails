package toughasnails.thirst;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import toughasnails.api.PlayerStat;

public class ThirstStats extends PlayerStat
{
    private int thirstLevel;
    private int prevThirstLevel;
    
    public ThirstStats(String identifier)
    {
        super(identifier);
    }

    @Override
    public void init(EntityPlayer player, World world)
    {
        this.thirstLevel = 20;
    }
    
    @Override
    public void update(EntityPlayer player, World world)
    {  
        this.thirstLevel = 5;
    }
    
    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        compound.setInteger("thirstLevel", this.thirstLevel);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    { 
        if (compound.hasKey("thirstLevel"))
        {
            this.thirstLevel = compound.getInteger("thirstLevel");
        }
    }

    @Override
    public boolean shouldUpdateClient()
    {
        return this.prevThirstLevel != this.thirstLevel;
    }
    
    public int getThirstLevel()
    {
        return this.thirstLevel;
    }
}

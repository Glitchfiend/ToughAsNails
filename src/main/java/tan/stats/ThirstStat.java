package tan.stats;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import tan.api.TANStat;

public class ThirstStat extends TANStat
{
    private int thirstLevel;

    private float thirstExhaustionLevel;

    private int thirstTimer;

    @Override
    public void update()
    {
        if (player.capabilities.isCreativeMode) return;
        
        NBTTagCompound thirstCompound = tanData.getCompoundTag(getStatName());
        
        thirstLevel = thirstCompound.getInteger("thirstLevel");
        thirstExhaustionLevel = thirstCompound.getFloat("thirstExhaustionLevel");
        thirstTimer = thirstCompound.getInteger("thirstTimer");
        
        int difficulty = player.worldObj.difficultySetting;

        if (this.thirstExhaustionLevel > 4.0F)
        {
            this.thirstExhaustionLevel -= 4.0F;

            if (difficulty > 0)
            {
                this.thirstLevel = Math.max(this.thirstLevel - 1, 0);
            }
        }

        if (this.thirstLevel <= 0)
        {
            ++this.thirstTimer;

            if (this.thirstTimer >= 80)
            {
                if (player.getHealth() > 10.0F || difficulty >= 3 || player.getHealth() > 1.0F && difficulty >= 2)
                {
                    player.attackEntityFrom(DamageSource.starve, 1.0F);
                }

                this.thirstTimer = 0;
            }
        }
        else
        {
            this.thirstTimer = 0;
        }
        
        if (world.rand.nextFloat() <= 0.05F)
        {
            addExhaustion(0.1F);
        }
        
        thirstCompound.setInteger("thirstLevel", thirstLevel);
        thirstCompound.setFloat("thirstExhaustionLevel", thirstExhaustionLevel);
        thirstCompound.setInteger("thirstTimer", thirstTimer);
        
        updatePlayerData(tanData, player);
    }

    @Override
    public void setDefaults()
    {
        NBTTagCompound thirstCompound = new NBTTagCompound();
        
        thirstCompound.setInteger("thirstLevel", 20);
        thirstCompound.setFloat("thirstExhaustionLevel", 0F);
        thirstCompound.setInteger("thirstTimer", 0);
        
        setDefaultCompound(getStatName(), thirstCompound);
    }
    
    public void addExhaustion(float amount)
    {
        this.thirstExhaustionLevel = Math.min(this.thirstExhaustionLevel + amount, 40.0F);
    }

    @Override
    public String getStatName()
    {
        return "Thirst";
    }
}

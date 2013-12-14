package tan.stats;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import tan.api.TANStat;

public class ThirstStat extends TANStat
{
    private int thirstLevel;

    private float thirstHydrationLevel;
    private float thirstExhaustionLevel;

    private int thirstTimer;

    @Override
    public void update()
    {
        if (player.capabilities.isCreativeMode) return;
        
        int i = player.worldObj.difficultySetting;

        if (this.thirstExhaustionLevel > 4.0F)
        {
            this.thirstExhaustionLevel -= 4.0F;

            if (this.thirstHydrationLevel > 0.0F)
            {
                this.thirstHydrationLevel = Math.max(this.thirstHydrationLevel - 1.0F, 0.0F);
            }
            else if (i > 0)
            {
                this.thirstLevel = Math.max(this.thirstLevel - 1, 0);
            }
        }

        if (this.thirstLevel <= 0)
        {
            ++this.thirstTimer;

            if (this.thirstTimer >= 80)
            {
                if (player.getHealth() > 10.0F || i >= 3 || player.getHealth() > 1.0F && i >= 2)
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
        
        if (world.rand.nextInt(50) == 0)
        {
            addExhaustion(3F);
        }
        
        System.out.println(thirstExhaustionLevel + " " + thirstHydrationLevel);
    }
    
    @Override
    public void readNBT(NBTTagCompound tanData)
    {
        NBTTagCompound thirstCompound = tanData.getCompoundTag(getStatName());
        
        thirstLevel = thirstCompound.getInteger("thirstLevel");
        thirstHydrationLevel = thirstCompound.getFloat("thirstHydrationLevel");
        thirstExhaustionLevel = thirstCompound.getFloat("thirstExhaustionLevel");
        thirstTimer = thirstCompound.getInteger("thirstTimer");
    }

    @Override
    public void writeNBT(NBTTagCompound tanData)
    {
        NBTTagCompound thirstCompound = new NBTTagCompound();
        
        thirstCompound.setInteger("thirstLevel", thirstLevel);
        thirstCompound.setFloat("thirstHydrationLevel", thirstHydrationLevel);
        thirstCompound.setFloat("thirstExhaustionLevel", thirstExhaustionLevel);
        thirstCompound.setInteger("thirstTimer", thirstTimer);
        
        tanData.setCompoundTag(getStatName(), thirstCompound);
        
        updatePlayerData(tanData, player);
    }

    @Override
    public void setDefaults(NBTTagCompound tanData)
    {
        NBTTagCompound thirstCompound = new NBTTagCompound();
        
        thirstCompound.setInteger("thirstLevel", 20);
        thirstCompound.setFloat("thirstHydrationLevel", 10F);
        thirstCompound.setFloat("thirstExhaustionLevel", 0F);
        thirstCompound.setInteger("thirstTimer", 0);
        
        setDefaultCompound(tanData, getStatName(), thirstCompound);
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

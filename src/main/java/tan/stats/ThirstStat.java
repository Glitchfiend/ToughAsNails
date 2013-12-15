package tan.stats;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import tan.api.TANStat;
import tan.api.event.thirst.ThirstEvent;
import tan.core.TANDamageSource;

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
        
        ThirstEvent thirstEvent = new ThirstEvent(this.player, thirstLevel, thirstHydrationLevel, thirstExhaustionLevel, thirstTimer);
        
        MinecraftForge.EVENT_BUS.post(thirstEvent);
        
        thirstLevel = thirstEvent.thirstLevel;
        thirstHydrationLevel = thirstEvent.thirstHydrationLevel;
        thirstExhaustionLevel = thirstEvent.thirstExhaustionLevel;
        
        int i = player.worldObj.difficultySetting;

        if (this.thirstExhaustionLevel > 2.0F)
        {
            this.thirstExhaustionLevel -= 2.0F;

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
                    player.attackEntityFrom(TANDamageSource.dehydration, 1.0F);
                }

                this.thirstTimer = 0;
            }
        }
        else
        {
            this.thirstTimer = 0;
        }
    }
    
    @Override
    public void readNBT(NBTTagCompound tanData)
    {
        NBTTagCompound thirstCompound = tanData.getCompoundTag("thirst");
        
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
        
        tanData.setCompoundTag("thirst", thirstCompound);
        
        updatePlayerData(tanData, player);
    }

    @Override
    public void setDefaults(NBTTagCompound tanData)
    {
        NBTTagCompound thirstCompound = new NBTTagCompound();
        
        thirstCompound.setInteger("thirstLevel", 20);
        thirstCompound.setFloat("thirstHydrationLevel", 7F);
        thirstCompound.setFloat("thirstExhaustionLevel", 0F);
        thirstCompound.setInteger("thirstTimer", 0);
        
        setDefaultCompound(tanData, "thirst", thirstCompound);
    }
    
    public static float addExhaustion(float thirstExhaustionLevel, float amount)
    {
        return Math.min(thirstExhaustionLevel + amount, 40.0F);
    }
}

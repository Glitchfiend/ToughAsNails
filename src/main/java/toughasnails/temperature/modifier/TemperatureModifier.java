package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.ITemperatureModifier;
import toughasnails.api.temperature.Temperature;

public abstract class TemperatureModifier implements ITemperatureModifier
{
    private final String id;

    public TemperatureModifier(String id)
    {
        this.id = id;
    }

    @Override
    public Temperature applyEnvironmentModifiers(World world, BlockPos pos, Temperature initialTemperature, IModifierMonitor monitor)
    {
        return initialTemperature;
    }

    @Override
    public Temperature applyPlayerModifiers(EntityPlayer player, Temperature initialTemperature, IModifierMonitor monitor)
    {
        return applyEnvironmentModifiers(player.world, player.getPosition(), initialTemperature, monitor);
    }

    @Override
    public abstract boolean isPlayerSpecific();

    @Override
    public String getId()
    {
        return this.id;
    }

    public static class ExternalModifier implements INBTSerializable<NBTTagCompound>
    {
        private String name;
        private int amount;
        private int rate;
        private int endTime;
        
        public ExternalModifier() {}
        
        public ExternalModifier(String name, int amount, int rate, int endTime)
        {
            this.name = name;
            this.amount = amount;
            this.rate = rate;
            this.endTime = endTime;
        }
        
        public String getName()
        {
            return this.name;
        }
        
        public int getAmount()
        {
            return this.amount;
        }
        
        public void setAmount(int amount)
        {
            this.amount = amount;
        }
        
        public int getRate()
        {
            return this.rate;
        }
        
        public void setRate(int rate)
        {
            this.rate = rate;
        }
        
        public int getEndTime()
        {
            return this.endTime;
        }
        
        public void setEndTime(int time)
        {
            this.endTime = time;
        }
    
        @Override
        public NBTTagCompound serializeNBT() 
        {
            NBTTagCompound compound = new NBTTagCompound();
            
            compound.setString("Name", this.name);
            compound.setInteger("Amount", this.amount);
            compound.setInteger("Rate", this.rate);
            compound.setInteger("EndTime", this.endTime);
            
            return compound;
        }
    
        @Override
        public void deserializeNBT(NBTTagCompound nbt) 
        {
            this.name = nbt.getString("Name");
            this.amount = nbt.getInteger("Amount");
            this.rate = nbt.getInteger("Rate");
            this.endTime = nbt.getInteger("EndTime");
        }
    }
}

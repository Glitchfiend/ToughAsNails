package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureTrend;

public abstract class TemperatureModifier
{
    protected final TemperatureDebugger debugger;
    
    protected TemperatureModifier(TemperatureDebugger debugger)
    {
        this.debugger = debugger;
    }
    
    public abstract int modifyChangeRate(World world, EntityPlayer player, int changeRate, TemperatureTrend trend);
    public abstract Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature);
    
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
        
        public int getRate()
        {
            return this.rate;
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

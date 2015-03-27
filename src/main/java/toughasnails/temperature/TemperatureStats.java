package toughasnails.temperature;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class TemperatureStats implements IExtendedEntityProperties
{
    private int temperatureLevel;
    private int prevTemperatureLevel;
    
    @Override
    public void init(Entity entity, World world)
    {
        this.temperatureLevel = TemperatureScale.getScaleTotal() / 2;
        this.prevTemperatureLevel = this.temperatureLevel;
    }
    
    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        compound.setInteger("TemperatureLevel", this.temperatureLevel);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
       if (compound.hasKey("TemperatureLevel"))
       {
           this.temperatureLevel = compound.getInteger("TemperatureLevel");
       }
    }
    
    public void addTemperature(int temperature)
    {
        this.temperatureLevel = Math.max(Math.min(TemperatureScale.getScaleTotal(), this.temperatureLevel + temperature), 0);
    }
    
    public void setTemperature(int temperature)
    {
        this.temperatureLevel = temperature;
    }
    
    public void setPrevTemperature(int temperature)
    {
        this.prevTemperatureLevel = temperature;
    }
    
    public TemperatureInfo getTemperature()
    {
        return new TemperatureInfo(this.temperatureLevel);
    }
    
    public TemperatureInfo getPrevTemperature()
    {
        return new TemperatureInfo(this.prevTemperatureLevel);
    }
}

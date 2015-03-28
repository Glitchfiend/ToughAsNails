package toughasnails.temperature;

import toughasnails.temperature.modifier.BiomeTemperatureModifier;
import toughasnails.temperature.modifier.ITemperatureModifier;
import toughasnails.temperature.modifier.WaterModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class TemperatureStats implements IExtendedEntityProperties
{
    public static final int BASE_TEMPERATURE_CHANGE_TICKS = 2400;
    
    private int temperatureLevel;
    private int prevTemperatureLevel;
    private int temperatureTimer;
    
    private ITemperatureModifier biomeTemperatureModifier;
    private ITemperatureModifier waterModifier;
    
    @Override
    public void init(Entity entity, World world)
    {
        this.temperatureLevel = TemperatureScale.getScaleTotal() / 2;
        this.prevTemperatureLevel = this.temperatureLevel;
        
        this.biomeTemperatureModifier = new BiomeTemperatureModifier();
        this.waterModifier = new WaterModifier();
    }
    
    public void update(World world, EntityPlayer player)
    {
        TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");
        TemperatureInfo temperature = temperatureStats.getTemperature();
        
        int newTempChangeTicks = BASE_TEMPERATURE_CHANGE_TICKS;
        
        newTempChangeTicks = waterModifier.modifyChangeRate(world, player, newTempChangeTicks);
        
        if (++temperatureTimer >= newTempChangeTicks)
        {
            TemperatureInfo targetTemperature = biomeTemperatureModifier.modifyTarget(world, player, temperature);

            targetTemperature = waterModifier.modifyTarget(world, player, targetTemperature);
            
            temperatureStats.addTemperature((int)Math.signum(targetTemperature.getScalePos() - temperature.getScalePos()));
            temperatureTimer = 0;
        }
    }
    
    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        compound.setInteger("temperatureLevel", this.temperatureLevel);
        compound.setInteger("temperatureTimer", this.temperatureTimer);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
       if (compound.hasKey("temperatureLevel"))
       {
           this.temperatureLevel = compound.getInteger("temperatureLevel");
           this.temperatureTimer = compound.getInteger("temperatureTimer");
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

package toughasnails.temperature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import toughasnails.api.PlayerStat;
import toughasnails.api.TANPotions;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureScale.TemperatureRange;
import toughasnails.temperature.modifier.BiomeModifier;
import toughasnails.temperature.modifier.ObjectProximityModifier;
import toughasnails.temperature.modifier.PlayerStateModifier;
import toughasnails.temperature.modifier.TemperatureModifier;
import toughasnails.temperature.modifier.TimeModifier;
import toughasnails.temperature.modifier.WeatherModifier;

public class TemperatureStats extends PlayerStat
{
    public static final int BASE_TEMPERATURE_CHANGE_TICKS = 1200;
    
    private int temperatureLevel;
    private int prevTemperatureLevel;
    private int temperatureTimer;
    
    private TemperatureModifier biomeModifier;
    private TemperatureModifier playerStateModifier;
    private TemperatureModifier objectProximityModifier;
    private TemperatureModifier weatherModifier;
    private TemperatureModifier timeModifier;
    
    public final TemperatureDebugger debugger = new TemperatureDebugger();
    
    public TemperatureStats(String identifier)
    {
        super(identifier);
    }
    
    @Override
    public void init(EntityPlayer player, World world)
    {
        this.temperatureLevel = TemperatureScale.getScaleTotal() / 2;
        this.prevTemperatureLevel = this.temperatureLevel;
        
        this.biomeModifier = new BiomeModifier(debugger);
        this.playerStateModifier = new PlayerStateModifier(debugger);
        this.objectProximityModifier = new ObjectProximityModifier(debugger);
        this.weatherModifier = new WeatherModifier(debugger);
        this.timeModifier = new TimeModifier(debugger);
    }
    
    @Override
    public void update(EntityPlayer player, World world, Phase phase)
    {
        if (phase == Phase.END)
        {
            debugger.start(Modifier.BODY_TEMPERATURE_TARGET, 0);
            debugger.end(this.temperatureLevel);

            int newTempChangeTicks = BASE_TEMPERATURE_CHANGE_TICKS;

            newTempChangeTicks = biomeModifier.modifyChangeRate(world, player, newTempChangeTicks);
            newTempChangeTicks = playerStateModifier.modifyChangeRate(world, player, newTempChangeTicks);
            newTempChangeTicks = objectProximityModifier.modifyChangeRate(world, player, newTempChangeTicks);
            newTempChangeTicks = weatherModifier.modifyChangeRate(world, player, newTempChangeTicks);
            newTempChangeTicks = timeModifier.modifyChangeRate(world, player, newTempChangeTicks);

            newTempChangeTicks = Math.max(20, newTempChangeTicks);

            boolean incrementTemperature = ++temperatureTimer >= newTempChangeTicks;
            boolean updateDebug = debugger.isGuiVisible() && ++debugger.debugTimer % 5 == 0;

            debugger.temperatureTimer = temperatureTimer;
            debugger.changeTicks = newTempChangeTicks;

            if (incrementTemperature || updateDebug)
            {
                TemperatureInfo targetTemperature = biomeModifier.modifyTarget(world, player, this.getTemperature());
                targetTemperature = playerStateModifier.modifyTarget(world, player, targetTemperature);
                targetTemperature = objectProximityModifier.modifyTarget(world, player, targetTemperature);
                targetTemperature = weatherModifier.modifyTarget(world, player, targetTemperature);
                targetTemperature = timeModifier.modifyTarget(world, player, targetTemperature);

                debugger.targetTemperature = targetTemperature.getScalePos();

                targetTemperature = new TemperatureInfo(MathHelper.clamp_int(targetTemperature.getScalePos(), 0, TemperatureScale.getScaleTotal()));

                if (incrementTemperature)
                {
                    this.addTemperature((int)Math.signum(targetTemperature.getScalePos() - this.temperatureLevel));
                    this.temperatureTimer = 0;
                }
            }

            addPotionEffects(player);

            if (updateDebug)
            {
                //This works because update is only called if !world.isRemote
                debugger.finalize((EntityPlayerMP)player);
            }
        }
    }
    
    private void addPotionEffects(EntityPlayer player)
    {
        int icyHalf = (TemperatureRange.ICY.getRangeSize() - 1) / 2;
        int hotHalf = (TemperatureRange.HOT.getRangeSize() - 1) / 2;
        
        if (temperatureLevel <= icyHalf && (temperatureLevel < prevTemperatureLevel || !player.isPotionActive(TANPotions.hypothermia.id)))
        {
            float icyDelta = 1.0F - temperatureLevel * 1.0F / icyHalf;

            player.removePotionEffect(TANPotions.hypothermia.id);
            player.addPotionEffect(new PotionEffect(TANPotions.hypothermia.id, (int)(2400 * icyDelta), (int)(3 * icyDelta)));
        }
        else if (temperatureLevel >= TemperatureScale.getRangeStart(TemperatureRange.HOT) + hotHalf && (temperatureLevel > prevTemperatureLevel || !player.isPotionActive(TANPotions.hyperthermia.id)))
        {
            float hotDelta = (temperatureLevel - (TemperatureScale.getRangeStart(TemperatureRange.HOT) + hotHalf) - 1) * 1.0F / hotHalf;
            
            player.removePotionEffect(TANPotions.hyperthermia.id);
            player.addPotionEffect(new PotionEffect(TANPotions.hyperthermia.id, (int)(2400 * hotDelta), (int)(3 * hotDelta)));
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
    
    @Override
    public boolean shouldUpdateClient()
    {
        return this.prevTemperatureLevel != this.temperatureLevel;
    }
    
    @Override
    public void onSendClientUpdate()
    {
        this.prevTemperatureLevel = this.temperatureLevel;
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
}

package toughasnails.temperature;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.TANCapabilities;
import toughasnails.api.TANPotions;
import toughasnails.api.stat.StatHandlerBase;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.api.temperature.TemperatureScale.TemperatureRange;
import toughasnails.api.config.GameplayOption;
import toughasnails.network.message.MessageUpdateStat;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.modifier.AltitudeModifier;
import toughasnails.temperature.modifier.ArmorModifier;
import toughasnails.temperature.modifier.BiomeModifier;
import toughasnails.temperature.modifier.ObjectProximityModifier;
import toughasnails.temperature.modifier.PlayerStateModifier;
import toughasnails.temperature.modifier.SeasonModifier;
import toughasnails.temperature.modifier.TemperatureModifier;
import toughasnails.temperature.modifier.TemperatureModifier.ExternalModifier;
import toughasnails.temperature.modifier.TimeModifier;
import toughasnails.temperature.modifier.WeatherModifier;

public class TemperatureHandler extends StatHandlerBase implements ITemperature
{
    public static final int TEMPERATURE_SCALE_MIDPOINT = TemperatureScale.getScaleTotal() / 2;
    // by default, temperature changes once every 30 seconds
    public static final int BASE_TEMPERATURE_CHANGE_TICKS = 600;
    /** The maximum number of ticks the temperature change rate can be reduced by.
     * By default this is set sp that the minimum change rate is at least 5 seconds.**/
    public static final int MAX_RATE_MODIFIER = 700;
    
    private int temperatureLevel;
    private int prevTemperatureLevel;
    private int temperatureTimer;
    
    private TemperatureModifier altitudeModifier;
    private TemperatureModifier armorModifier;
    private TemperatureModifier biomeModifier;
    private TemperatureModifier playerStateModifier;
    private TemperatureModifier objectProximityModifier;
    private TemperatureModifier weatherModifier;
    private TemperatureModifier timeModifier;
    private TemperatureModifier seasonModifier;

    private Set<TemperatureModifier> temperatureModifiers;
    private Map<String, TemperatureModifier.ExternalModifier> externalModifiers;
    
    public final TemperatureDebugger debugger = new TemperatureDebugger();
    
    public TemperatureHandler()
    {
        this.temperatureLevel = TemperatureScale.getScaleTotal() / 2;
        this.prevTemperatureLevel = this.temperatureLevel;

        this.temperatureModifiers = Sets.newHashSet(new AltitudeModifier(debugger), new ArmorModifier(debugger), new BiomeModifier(debugger),
                new PlayerStateModifier(debugger), new ObjectProximityModifier(debugger), new WeatherModifier(debugger), new TimeModifier(debugger),
                new SeasonModifier(debugger));

        this.externalModifiers = Maps.newHashMap();
    }
    
    @Override
    public void update(EntityPlayer player, World world, Phase phase)
    {
        if (phase == Phase.END && !world.isRemote)
        {
            int newTempChangeTicks = BASE_TEMPERATURE_CHANGE_TICKS;
            Temperature targetTemperature = getTargetTemperature(world, player);

            // the greater the difference between the current temperature and the target temperature,
            // the faster the rate should be
            double rateDelta = Math.abs((this.temperatureLevel - targetTemperature.getRawValue()) / TemperatureScale.getScaleTotal());
            newTempChangeTicks -= (int)(rateDelta * (double)MAX_RATE_MODIFIER);

            // temperature can't change at a rate faster than every second
            newTempChangeTicks = Math.max(20, newTempChangeTicks);

            boolean incrementTemperature = ++temperatureTimer >= newTempChangeTicks;
            boolean updateClient = ++debugger.debugTimer % 5 == 0;

            debugger.temperatureTimer = temperatureTimer;
            debugger.changeTicks = newTempChangeTicks;

            if (incrementTemperature && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE))
            {
                for (ExternalModifier modifier : this.externalModifiers.values())
                {
                    modifier.setEndTime(modifier.getEndTime() - this.temperatureTimer);
                }
            }
            
            if (incrementTemperature && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE))
            {
                this.addTemperature(new Temperature((int)Math.signum(targetTemperature.getRawValue() - this.temperatureLevel)));
                this.temperatureTimer = 0;
            }

            addPotionEffects(player);

            if (updateClient)
            {
                //This works because update is only called if !world.isRemote
                debugger.finalize((EntityPlayerMP)player);
            }
        }
    }

    private Temperature getTargetTemperature(World world, EntityPlayer player)
    {
        debugger.start(Modifier.EQUILIBRIUM_TARGET, 0);
        debugger.end(TemperatureScale.getScaleTotal() / 2);

        Temperature targetTemperature = new Temperature(TEMPERATURE_SCALE_MIDPOINT);

        for (TemperatureModifier modifier : temperatureModifiers)
        {
            modifier.modifyTarget(world, player, targetTemperature);
        }

        debugger.start(Modifier.CLIMATISATION_TARGET, targetTemperature.getRawValue());
        for (TemperatureModifier.ExternalModifier modifier : this.externalModifiers.values())
        {
            targetTemperature = new Temperature(targetTemperature.getRawValue() + modifier.getAmount());
        }
        debugger.end(targetTemperature.getRawValue());

        debugger.targetTemperature = targetTemperature.getRawValue();
        // temperature values can't be above or less than the scale allows
        // TODO: Possibly move this to the temperature class
        targetTemperature = new Temperature(MathHelper.clamp(targetTemperature.getRawValue(), 0, TemperatureScale.getScaleTotal()));

        return targetTemperature;
    }
    
    private void addPotionEffects(EntityPlayer player)
    {
        TemperatureRange range = TemperatureScale.getTemperatureRange(this.temperatureLevel);
        float multiplier = 1.0F;
        
        //The point from 0 to 1 at which potion effects begin in an extremity range
        float extremityDelta = (3.0F / 6.0F);
        
        //Start the hypo/hyperthermia slightly after the real ranges start
        int hypoRangeSize = (int)(TemperatureRange.ICY.getRangeSize() * extremityDelta);
        int hypoRangeStart = hypoRangeSize - 1;
        int hyperRangeSize = (int)(TemperatureRange.HOT.getRangeSize() * extremityDelta);
        int hyperRangeStart = (TemperatureScale.getScaleTotal() + 1) - hyperRangeSize;
        
        //Don't apply any negative effects whilst in creative mode
        if (!player.capabilities.isCreativeMode && (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE)))
        {
            if (this.temperatureLevel <= hypoRangeStart && (!player.isPotionActive(TANPotions.cold_resistance)) && (temperatureLevel < prevTemperatureLevel || !player.isPotionActive(TANPotions.hypothermia)))
            {
                multiplier = 1.0F - ((float)(this.temperatureLevel + 1) / (float)hypoRangeSize);
                player.removePotionEffect(TANPotions.hypothermia);
                player.addPotionEffect(new PotionEffect(TANPotions.hypothermia, (int)(1800 * multiplier) + 600, (int)(3 * multiplier + extremityDelta)));
            }
            else if (this.temperatureLevel >= hyperRangeStart && (!player.isPotionActive(TANPotions.heat_resistance)) && (temperatureLevel > prevTemperatureLevel || !player.isPotionActive(TANPotions.hyperthermia)))
            {
                multiplier = (float)(this.temperatureLevel - hyperRangeStart) / hyperRangeSize;
                player.removePotionEffect(TANPotions.hyperthermia);
                player.addPotionEffect(new PotionEffect(TANPotions.hyperthermia, (int)(1800 * multiplier) + 600, (int)(3 * multiplier)));
            }
        }
    }
    
    @Override
    public boolean hasChanged()
    {
        return this.prevTemperatureLevel != this.temperatureLevel;
    }
    
    @Override
    public void onSendClientUpdate()
    {
        this.prevTemperatureLevel = this.temperatureLevel;
    }
    
    @Override
    public IMessage createUpdateMessage()
    {
        NBTTagCompound data = (NBTTagCompound)TANCapabilities.TEMPERATURE.getStorage().writeNBT(TANCapabilities.TEMPERATURE, this, null);
        return new MessageUpdateStat(TANCapabilities.TEMPERATURE, data);
    }
    
    @Override
    public void setChangeTime(int ticks)
    {
        this.temperatureTimer = ticks;
    }
    
    @Override
    public int getChangeTime()
    {
        return this.temperatureTimer;
    }
    
    @Override
    public void setTemperature(Temperature temperature)
    {
        this.temperatureLevel = temperature.getRawValue();
    }
    
    @Override
    public void addTemperature(Temperature difference)
    {
        this.temperatureLevel = Math.max(Math.min(TemperatureScale.getScaleTotal(), this.temperatureLevel + difference.getRawValue()), 0);
    }
    
    @Override
    public void applyModifier(String name, int amount, int rate, int duration)
    {
        if (this.externalModifiers.containsKey(name))
        {
            ExternalModifier modifier = this.externalModifiers.get(name);
            modifier.setEndTime(this.temperatureTimer + duration);
        }
        else
        {
            TemperatureModifier.ExternalModifier modifier = new TemperatureModifier.ExternalModifier(name, amount, rate, this.temperatureTimer + duration);
            this.externalModifiers.put(name, modifier);
        }
    }
    
    @Override
    public boolean hasModifier(String name) 
    {
        return this.externalModifiers.containsKey(name);
    }
    
    @Override
    public ImmutableMap<String, ExternalModifier> getExternalModifiers() 
    {
        return ImmutableMap.copyOf(this.externalModifiers);
    }

    @Override
    public void setExternalModifiers(Map<String, ExternalModifier> externalModifiers) 
    {
        this.externalModifiers = externalModifiers;
    }
    
    @Override
    public Temperature getTemperature()
    {
        return new Temperature(this.temperatureLevel);
    }
}

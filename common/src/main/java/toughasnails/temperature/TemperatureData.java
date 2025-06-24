/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.init.ModConfig;

import java.util.HashSet;
import java.util.Set;

public class TemperatureData implements ITemperature
{
    public static final TemperatureLevel DEFAULT_LEVEL = TemperatureLevel.NEUTRAL;

    private TemperatureLevel level = DEFAULT_LEVEL;
    private TemperatureLevel targetLevel = DEFAULT_LEVEL;
    private int positionalChangeDelayTicks;
    private int ticksHyperthermic;
    private int ticksDry;
    private int extremityDelayTicks;

    private Set<BlockPos> nearbyThermoregulators = new HashSet<>();
    private TemperatureLevel lastTemperature = DEFAULT_LEVEL;
    private int lastHyperthermiaTicks;
    private Set<BlockPos> lastNearbyThermoregulators = new HashSet<>();

    public void addAdditionalSaveData(ValueOutput output)
    {
        if (ModConfig.temperature.enableTemperature)
        {
            output.putInt("temperatureLevel", this.getLevel().ordinal());
            output.putInt("targetTemperatureLevel", this.getTargetLevel().ordinal());
            output.putInt("changeDelayTicks", this.getChangeDelayTicks());
            output.putInt("hyperthermiaTicks", this.getHyperthermiaTicks());
            output.putInt("extremityDelayTicks", this.getExtremityDelayTicks());
            output.putInt("dryTicks", this.getDryTicks());
        }
        else
        {
            // Save default values
            output.putInt("temperatureLevel", TemperatureData.DEFAULT_LEVEL.ordinal());
            output.putInt("targetTemperatureLevel", TemperatureData.DEFAULT_LEVEL.ordinal());
            output.putInt("changeDelayTicks", 0);
            output.putInt("hyperthermiaTicks", 0);
            output.putInt("extremityDelayTicks", 0);
            output.putInt("dryTicks", 0);
        }
    }

    public void readAdditionalSaveData(ValueInput input)
    {
        if (ModConfig.temperature.enableTemperature)
        {
            this.setLevel(TemperatureLevel.values()[input.getInt("temperatureLevel").orElse(TemperatureData.DEFAULT_LEVEL.ordinal())]);
            this.setTargetLevel(TemperatureLevel.values()[input.getInt("targetTemperatureLevel").orElse(TemperatureData.DEFAULT_LEVEL.ordinal())]);
            this.setChangeDelayTicks(input.getInt("changeDelayTicks").orElse(0));
            this.setHyperthermiaTicks(input.getInt("hyperthermiaTicks").orElse(0));
            this.setExtremityDelayTicks(input.getInt("extremityDelayTicks").orElse(0));
        }
        else
        {
            // Use default values if temperature is disabled
            this.setLevel(TemperatureData.DEFAULT_LEVEL);
            this.setTargetLevel(TemperatureData.DEFAULT_LEVEL);
            this.setChangeDelayTicks(0);
            this.setHyperthermiaTicks(0);
            this.setExtremityDelayTicks(0);
            this.setDryTicks(0);
        }
    }

    @Override
    public TemperatureLevel getLevel()
    {
        return this.level;
    }

    @Override
    public int getHyperthermiaTicks()
    {
        return this.ticksHyperthermic;
    }

    @Override
    public int getExtremityDelayTicks()
    {
        return this.extremityDelayTicks;
    }

    @Override
    public int getDryTicks() {
        return ticksDry;
    }

    @Override
    public TemperatureLevel getLastLevel()
    {
        return this.lastTemperature;
    }

    @Override
    public TemperatureLevel getTargetLevel()
    {
        return this.targetLevel;
    }

    @Override
    public int getChangeDelayTicks()
    {
        return this.positionalChangeDelayTicks;
    }

    @Override
    public int getLastHyperthermiaTicks()
    {
        return this.lastHyperthermiaTicks;
    }

    @Override
    public Set<BlockPos> getLastNearbyThermoregulators()
    {
        return this.lastNearbyThermoregulators;
    }

    @Override
    public Set<BlockPos> getNearbyThermoregulators()
    {
        return this.nearbyThermoregulators;
    }

    @Override
    public void setLevel(TemperatureLevel level)
    {
        this.level = level;
    }

    @Override
    public void setHyperthermiaTicks(int ticks)
    {
        this.ticksHyperthermic = ticks;
    }

    @Override
    public void setExtremityDelayTicks(int ticks)
    {
        this.extremityDelayTicks = ticks;
    }

    @Override
    public void setDryTicks(int ticks) {
        ticksDry = ticks;
    }

    @Override
    public void setLastLevel(TemperatureLevel level)
    {
        this.lastTemperature = level;
    }

    @Override
    public void setTargetLevel(TemperatureLevel level)
    {
        this.targetLevel = level;
    }

    @Override
    public void setChangeDelayTicks(int ticks)
    {
        this.positionalChangeDelayTicks = ticks;
    }

    @Override
    public void setLastHyperthermiaTicks(int ticks)
    {
        this.lastHyperthermiaTicks = ticks;
    }

    @Override
    public void setLastNearbyThermoregulators(Set<BlockPos> values)
    {
        this.lastNearbyThermoregulators = values;
    }

    @Override
    public void setNearbyThermoregulators(Set<BlockPos> values)
    {
        this.nearbyThermoregulators = values;
    }
}

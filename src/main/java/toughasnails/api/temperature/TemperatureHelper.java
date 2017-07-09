/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.temperature;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.ITemperature;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TemperatureHelper 
{
    private static Map<String, ITemperatureModifier> modifiers = Maps.newHashMap();
    private static final IModifierMonitor DUMMY_MONITOR = new IModifierMonitor()
    {
        @Override
        public void addEntry(Context context) {}

        @Override
        public void setTargetTemperature(Temperature temperature) {}
    };

    /**
     * Register a temperature modifier. The modifier's id must be set.
     *
     * @param modifier The modifier to register
     * @return true on success, false on failure
     */
    public static boolean registerTemperatureModifier(ITemperatureModifier modifier)
    {
        if (modifier.getId() == null || modifiers.containsKey(modifier.getId()))
        {
            return false;
        }

        modifiers.put(modifier.getId(), modifier);
        return true;
    }

    /**
     * Get the current map of registered temperature modifiers.
     *
     * @return An immutable map of temperature modifiers
     */
    public static ImmutableMap<String, ITemperatureModifier> getTemperatureModifiers()
    {
        return ImmutableMap.copyOf(modifiers);
    }

    /** Get this player's temperature data.
     *
     * @param player The player to obtain data from
     * @return The player's temperature data
     */
    public static ITemperature getTemperatureData(EntityPlayer player)
    {
        return player.getCapability(TANCapabilities.TEMPERATURE, null);
    }

    /**
     * Get the target temperature based on the provided position.
     *
     * @param world The world to check the temperature of
     * @param pos The position to check the temperature at
     * @param monitor The modifier monitor (may be null)
     * @return The temperature at the provided position
     */
    public static Temperature getTargetAtPos(@Nonnull World world, @Nonnull BlockPos pos, @Nullable IModifierMonitor monitor)
    {
        if (monitor == null) monitor = DUMMY_MONITOR;

        int targetTemperature = TemperatureScale.getScaleMidpoint();
        monitor.addEntry(new IModifierMonitor.Context("equilibrium", "Equilibrium", new Temperature(0), new Temperature(TemperatureScale.getScaleMidpoint())));

        for (ITemperatureModifier modifier : TemperatureHelper.getTemperatureModifiers().values())
        {
            if (!modifier.isPlayerSpecific())
                targetTemperature = modifier.applyEnvironmentModifiers(world, pos, new Temperature(targetTemperature), monitor).getRawValue();
        }

        monitor.setTargetTemperature(new Temperature(targetTemperature));
        return new Temperature(MathHelper.clamp(targetTemperature, 0, TemperatureScale.getScaleTotal()));
    }

    /**
     * Get all temperature regulators in this world.
     *
     * @param world The world
     * @return A list of temperature regulators
     */
    public static List<ITemperatureRegulator> getTemperatureRegulators(World world)
    {
        List<ITemperatureRegulator> list = Lists.newArrayList();
        
        for (TileEntity tileEntity : world.tickableTileEntities)
        {
            if (tileEntity instanceof ITemperatureRegulator) list.add((ITemperatureRegulator)tileEntity);
        }
        
        return list;
    }

    /**
     * Check if this position is climatised for at least the provided temperature.
     *
     * @param world The world
     * @param pos The position to check
     * @param temperature The desired temperature
     * @return true if climatised for the given temperature, false if not
     */
    public static boolean isPosClimatisedForTemp(World world, BlockPos pos, Temperature temperature)
    {
        for (ITemperatureRegulator regulator : getTemperatureRegulators(world))
        {
            if (regulator.getRegulatedTemperature().getRawValue() >= temperature.getRawValue() && regulator.isPosRegulated(pos))
            {
                return true;
            }
        }
        
        return false;
    }
}

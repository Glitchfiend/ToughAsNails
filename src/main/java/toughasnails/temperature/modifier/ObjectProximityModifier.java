package toughasnails.temperature.modifier;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.api.temperature.Temperature;
import toughasnails.config.TANConfig;
import toughasnails.config.temperature.BlockTemperatureData;
import toughasnails.util.BlockStateUtils;

import java.util.ArrayList;

//TODO: Replace this with something better
public class ObjectProximityModifier extends TemperatureModifier
{
    public ObjectProximityModifier(String id)
    {
        super(id);
    }

    @Override
    public Temperature applyEnvironmentModifiers(World world, BlockPos pos, Temperature initialTemperature, IModifierMonitor monitor)
    {
        int temperatureLevel = initialTemperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;

        float blockTemperatureModifier = 0.0F;

        for (int x = -3; x <= 3; x++)
        {
            for (int y = -2; y <= 2; y++)
            {
                for (int z = -3; z <= 3; z++)
                {
                    BlockPos pos2 = pos.add(x, y - 1, z);
                    IBlockState state = world.getBlockState(pos2);
                    float mod = getBlockTemperature(world, pos2, state);

                    // use the most drastic temperature affecting block's temperature
                    if (Math.abs(mod) > Math.abs(blockTemperatureModifier))
                    {
                        blockTemperatureModifier = mod;
                    }
                }
            }
        }

        newTemperatureLevel += blockTemperatureModifier;
        monitor.addEntry(new IModifierMonitor.Context(this.getId(), "Nearby Blocks", initialTemperature, new Temperature(newTemperatureLevel)));

        return new Temperature(newTemperatureLevel);
    }

    public static float getBlockTemperature(World world, BlockPos pos, IBlockState state)
    {
        Material material = state.getMaterial();
        Biome biome = world.getBiome(pos);

        ResourceLocation registryName = state.getBlock().getRegistryName();
        if (registryName == null) return 0.0F;
        String blockName = state.getBlock().getRegistryName().toString();

        //Blocks
        if (TANConfig.blockTemperatureData.containsKey(blockName))
        {
            ArrayList<BlockTemperatureData> blockTempData = TANConfig.blockTemperatureData.get(blockName);

            //Check if block has relevant state: 
            for (BlockTemperatureData tempData : blockTempData)
            {
                if (tempData.predicate.apply(state))
                {
                    return tempData.blockTemperature;
                }
            }

            // If no matching states, then block is at ambient temperature:
            return 0.0F;
        }

        //Handle materials, but only if we didn't already find an actual block to use: 
        if (material == Material.FIRE)
        {
            return TANConfig.materialTemperatureData.fire;
        }

        return 0.0F;
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return false;
    }
}

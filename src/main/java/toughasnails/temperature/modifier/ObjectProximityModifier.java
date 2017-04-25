package toughasnails.temperature.modifier;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.Temperature;
import toughasnails.config.GameplayConfigurationHandler;
import toughasnails.temperature.BlockTemperatureData;
import toughasnails.temperature.BlockTemperatureStateData;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureTrend;

//TODO: Replace this with something better
public class ObjectProximityModifier extends TemperatureModifier
{
    public ObjectProximityModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }

    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate, TemperatureTrend trend)
    {
        int newChangeRate = changeRate;
        BlockPos playerPos = player.getPosition();
        
        int tempSourceBlocks = 0;
        
        //System.out.println(new Calendar(world).getSubSeason());
        
        for (int x = -3; x <= 3; x++)
        {
            for (int y = -2; y <= 2; y++)
            {
                for (int z = -3; z <= 3; z++)
                {
                    BlockPos pos = playerPos.add(x, y - 1, z);
                    IBlockState state = world.getBlockState(pos);

                    if (getBlockTemperature(player, state) != 0.0F) tempSourceBlocks++;
                }
            }
        }
        
        debugger.start(Modifier.NEARBY_BLOCKS_RATE, newChangeRate);
        newChangeRate -= tempSourceBlocks * 20;
        debugger.end(newChangeRate);
        
        return newChangeRate;
    }

    @Override
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature)
    {
        int temperatureLevel = temperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;
        BlockPos playerPos = player.getPosition();
        
        float blockTemperatureModifier = 0.0F;
        
        for (int x = -3; x <= 3; x++)
        {
            for (int y = -2; y <= 2; y++)
            {
                for (int z = -3; z <= 3; z++)
                {
                    BlockPos pos = playerPos.add(x, y - 1, z);
                    IBlockState state = world.getBlockState(pos);

                    blockTemperatureModifier += getBlockTemperature(player, state);
                }
            }
        }
        
        debugger.start(Modifier.NEARBY_BLOCKS_TARGET, newTemperatureLevel);
        newTemperatureLevel += blockTemperatureModifier;
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }
    
    public static float getBlockTemperature(EntityPlayer player, IBlockState state)
    {
        World world = player.worldObj;
        Material material = state.getMaterial();
        Biome biome = world.getBiomeGenForCoords(player.getPosition());
        
        String blockName = state.getBlock().getRegistryName().toString();
        
        //Blocks
        if (GameplayConfigurationHandler.blockTemperatureData.containsKey(blockName))
        {
        	BlockTemperatureData relevantTempData = GameplayConfigurationHandler.blockTemperatureData.get(blockName);
        	
        	//Check if block has relevant state: 
        	for (BlockTemperatureStateData stateTempData : relevantTempData.stateTemperatures)
        	{
        		for (IProperty<?> blockProperty : state.getProperties().keySet())
        		{
            		if (blockProperty.getName().equalsIgnoreCase(stateTempData.propertyName))
            		{
            			if (state.getValue(blockProperty).toString().equalsIgnoreCase(stateTempData.propertyValue))
            			{
            				return stateTempData.blockTemperature;
            			}
            		}
            	}
        	}
        	
        	//Use default temperature if no relevant state:
        	return relevantTempData.blockBaseTemperature;
        }
        
        //Handle materials, but only if we didn't already find an actual block to use: 
        if (material == Material.FIRE)
        {
            return GameplayConfigurationHandler.materialTemperatureData.FIRE;
        }
        
        return 0.0F;
    }
}

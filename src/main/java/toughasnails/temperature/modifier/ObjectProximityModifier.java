package toughasnails.temperature.modifier;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureInfo;

public class ObjectProximityModifier extends TemperatureModifier
{
    public ObjectProximityModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }

    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        int newChangeRate = changeRate;
        BlockPos playerPos = player.getPosition();

        debugger.start(Modifier.NEARBY_ENTITIES_RATE, newChangeRate);
        newChangeRate -= getNearbyEntityCount(world, player) * 20; 
        debugger.end(newChangeRate);
        
        int tempSourceBlocks = 0;
        
        for (int x = -2; x <= 2; x++)
        {
            for (int y = -2; y <= 2; y++)
            {
                for (int z = -2; z <= 2; z++)
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
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        int temperatureLevel = temperature.getScalePos();
        int newTemperatureLevel = temperatureLevel;
        BlockPos playerPos = player.getPosition();

        debugger.start(Modifier.NEARBY_ENTITIES_TARGET, newTemperatureLevel);
        newTemperatureLevel += getNearbyEntityCount(world, player);
        debugger.end(newTemperatureLevel);
        
        float blockTemperatureModifier = 0.0F;
        
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                for (int z = -1; z <= 1; z++)
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
        
        return new TemperatureInfo(newTemperatureLevel);
    }

    public static float getBlockTemperature(EntityPlayer player, IBlockState state)
    {
        World world = player.worldObj;
        Material material = state.getBlock().getMaterial();
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        
        if (material == Material.ice || material == Material.packedIce)
        {
            return -0.75F;
        }
        else if (material == Material.craftedSnow || material == Material.snow)
        {
            return -0.5F;
        }
        else if (material == Material.fire)
        {
            return 1.0F;
        }
        else if (material == Material.lava)
        {
            return 1.5F;
        }
        else if (player.worldObj.canBlockSeeSky(player.getPosition()) && biome.temperature > 1.0F && (world.getWorldTime() % 24000L) < 12000 && material == Material.sand)
        {
            /*If the player is above ground, the biome is hot and it's during the day, sand acts as a temperature source. This is to simulate sand being extremely hot 
             * in deserts during the day, but cold during the night.
             */
            
            return 0.5F;
        }
        
        return 0.0F;
    }
    
    private static int getNearbyEntityCount(World world, EntityPlayer player)
    {
        BlockPos playerPos = player.getPosition();
        
        AxisAlignedBB entitiesBox = new AxisAlignedBB(playerPos.add(-4, -4, -4), playerPos.add(4, 4, 4));
        
        return world.getEntitiesWithinAABBExcludingEntity(player, entitiesBox).size() * 2; 
    }
}

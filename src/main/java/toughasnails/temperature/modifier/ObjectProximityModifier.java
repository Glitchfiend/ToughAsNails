package toughasnails.temperature.modifier;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import toughasnails.temperature.TemperatureInfo;

public class ObjectProximityModifier implements ITemperatureModifier
{
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        int newChangeRate = changeRate;
        BlockPos playerPos = player.getPosition();
        AxisAlignedBB entitiesBox = new AxisAlignedBB(playerPos.add(-4, -4, -4), playerPos.add(4, 4, 4));
        
        newChangeRate -= (world.getEntitiesWithinAABBExcludingEntity(player, entitiesBox).size() * 2) * 20;
        
        int tempSourceBlocks = 0;
        
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    BlockPos pos = playerPos.add(x, y - 1, z);
                    IBlockState state = world.getBlockState(pos);

                    if (isTempSourceBlock(player, state)) tempSourceBlocks++;
                }
            }
        }
        
        newChangeRate -= tempSourceBlocks * 20;
        
        return newChangeRate;
    }

    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        return temperature;
    }

    public static boolean isTempSourceBlock(EntityPlayer player, IBlockState state)
    {
        World world = player.worldObj;
        Material material = state.getBlock().getMaterial();
        
        if (material == Material.ice || material == Material.fire || material == Material.lava || 
                material == Material.craftedSnow || material == Material.snow || material == Material.packedIce)
        {
            return true;
        }
        else if (player.worldObj.canBlockSeeSky(player.getPosition()) && material == Material.sand)
        {
            /*If the player is above ground, sand acts as a temperature source. This is to simulate sand being extremely hot 
             * in deserts during the day, but freezing cold during the night/
             */
            
            return true;
        }
        
        return false;
    }
}

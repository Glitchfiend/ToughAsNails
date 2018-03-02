package toughasnails.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.init.ModConfig;

public class TerrainUtils
{
    public static boolean isUnderground(World world, BlockPos pos)
    {
        return pos.getY() <= world.getHeight((int)pos.getX(), (int)pos.getZ()) - ModConfig.temperature.undergroundDepth;
    }
}

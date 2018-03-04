package toughasnails.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.init.ModConfig;

public class TerrainUtils
{
    public static boolean isUnderground(World world, BlockPos pos)
    {
        // Check vertical axis
        int verticalSurface = world.getHeight((int)pos.getX(), (int)pos.getZ());
        if (pos.getY() >= verticalSurface)
            return false;
        
        // Check horizontal axes
        int horizontalUndergroundDepth = 5; // TODO: add to config
        for (int x = pos.getX() - horizontalUndergroundDepth; x < pos.getX() + horizontalUndergroundDepth ; ++x)
        {
            if (world.canSeeSky(new BlockPos(x, pos.getY(), pos.getZ())))
                return false;
        }
        for (int z = pos.getZ() - horizontalUndergroundDepth; z < pos.getZ() + horizontalUndergroundDepth ; ++z)
        {
            if (world.canSeeSky(new BlockPos(pos.getX(), pos.getY(), z)))
                return false;
        }
        
        return true;
    }
    
    // Returns value between 0 (reached equilibrium depth) and 1 (surface)
    public static float getUndergroundCoefficient(World world, BlockPos pos)
    {
        if (!(isUnderground(world, pos)))
            return 1F;

        int verticalSurface = world.getHeight((int)pos.getX(), (int)pos.getZ());
        int depth = verticalSurface - pos.getY();
        int equilibriumDepth = ModConfig.temperature.equilibriumDepth;
        if (depth >= equilibriumDepth)
            return 0F;
        
        return 1F - (float)depth / (float)equilibriumDepth;
    }
    
    // Smoothes underground coefficient according to nearby blocks
    public static float getAverageUndergroundCoefficient(World world, BlockPos pos)
    {
        float buffer = 0F;
        int width = 3; // Square "radius" (should be smaller than "horizontalUndergroundDepth")
        int divider = 2;
        int count = 0;
        
        // Process nearby blocks
        for (int x = pos.getX() - width; x <= pos.getX() + width; x++)
        {
            for (int z = pos.getZ() - width; z <= pos.getZ() + width; z++)
            {
                // Optimisation: process 1 block out of n
                if (count % divider == 0)
                    buffer += getUndergroundCoefficient(world, new BlockPos(x, pos.getY(), z));
                count++;
            }
        }
        
        float avgCoeff = buffer / ((int)Math.pow(2F * width + 1, 2) / divider + 1F);
        return avgCoeff;
    }
}

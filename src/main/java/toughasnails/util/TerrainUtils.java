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
        int undergroundWidth = 5; // TODO: add to config
        for (int x = pos.getX() - undergroundWidth; x < pos.getX() + undergroundWidth ; ++x)
        {
            if (world.canSeeSky(new BlockPos(x, pos.getY(), pos.getZ())))
                return false;
        }
        for (int z = pos.getZ() - undergroundWidth; z < pos.getZ() + undergroundWidth ; ++z)
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
        int width = 3; // Cube "radius" (should be smaller than "undergroundWidth")
        int divider = 2;
        int count = 0;
        
        // Process nearby blocks belonging to a cube above the given position
        for (int x = pos.getX() - width; x <= pos.getX() + width; x++)
        {
            for (int y = pos.getY(); y <= pos.getY() + 2 * width; y++)
            {
                for (int z = pos.getZ() - width; z <= pos.getZ() + width; z++)
                {
                    // Optimisation: process 1 block out of n
                    if (count % divider == 0)
                    {
                        float coeff = getUndergroundCoefficient(world, new BlockPos(x, pos.getY(), z));
                        buffer += coeff;
                    }
                    count++;
                }
            }
        }
        
        float avgCoeff = buffer / ((int)Math.pow(2F * width + 1, 3) / divider + 1F);
        return avgCoeff;
    }
}

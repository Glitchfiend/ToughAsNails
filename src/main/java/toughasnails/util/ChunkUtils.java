package toughasnails.util;

import net.minecraft.world.World;

public class ChunkUtils
{
    public static boolean isChunkUnpopulated(World world, int cposX, int cposZ)
    {
        return !world.isChunkGeneratedAt(cposX, cposZ) || !world.getChunkFromChunkCoords(cposX, cposZ).isTerrainPopulated();
    }

    public static boolean hasUnpopulatedNeighbor(World world, int cposX, int cposZ)
    {
        if (isChunkUnpopulated(world, cposX + 1, cposZ))
            return true;
        if (isChunkUnpopulated(world, cposX + 1, cposZ + 1))
            return true;
        if (isChunkUnpopulated(world, cposX - 1, cposZ))
            return true;
        if (isChunkUnpopulated(world, cposX - 1, cposZ + 1))
            return true;
        if (isChunkUnpopulated(world, cposX, cposZ + 1))
            return true;
        if (isChunkUnpopulated(world, cposX, cposZ - 1))
            return true;
        if (isChunkUnpopulated(world, cposX + 1, cposZ - 1))
            return true;
        if (isChunkUnpopulated(world, cposX - 1, cposZ - 1))
            return true;

        return false;
    }
}

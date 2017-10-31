package toughasnails.util;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils
{
    public static boolean isChunkUnloadedOrUnpopulated(World world, int cposX, int cposZ)
    {
    	Chunk chunk = world.getChunkProvider().getLoadedChunk(cposX, cposZ);
    	
//        return !world.isChunkGeneratedAt(cposX, cposZ) || !world.getChunkFromChunkCoords(cposX, cposZ).isTerrainPopulated();
    	return chunk == null || !chunk.isTerrainPopulated();
    }

    public static boolean hasUnloadedOrUnpopulatedNeighbor(World world, int cposX, int cposZ)
    {
        if (isChunkUnloadedOrUnpopulated(world, cposX + 1, cposZ))
            return true;
        if (isChunkUnloadedOrUnpopulated(world, cposX + 1, cposZ + 1))
            return true;
        if (isChunkUnloadedOrUnpopulated(world, cposX - 1, cposZ))
            return true;
        if (isChunkUnloadedOrUnpopulated(world, cposX - 1, cposZ + 1))
            return true;
        if (isChunkUnloadedOrUnpopulated(world, cposX, cposZ + 1))
            return true;
        if (isChunkUnloadedOrUnpopulated(world, cposX, cposZ - 1))
            return true;
        if (isChunkUnloadedOrUnpopulated(world, cposX + 1, cposZ - 1))
            return true;
        if (isChunkUnloadedOrUnpopulated(world, cposX - 1, cposZ - 1))
            return true;

        return false;
    }
}

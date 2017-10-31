package toughasnails.util;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import toughasnails.season.ChunkKey;

public class ChunkUtils
{
    public static boolean isChunkUnloadedOrUnpopulated(World world, ChunkPos pos)
    {
//    	Chunk chunk = world.getChunkProvider().getLoadedChunk(cposX, cposZ);
    	
        return !world.isChunkGeneratedAt(pos.chunkXPos, pos.chunkZPos) || !world.getChunkFromChunkCoords(pos.chunkXPos, pos.chunkZPos).isTerrainPopulated();
//    	return chunk == null || !chunk.isTerrainPopulated();
    }

    public static boolean hasUnloadedOrUnpopulatedNeighbor(World world, ChunkPos pos)
    {
/*        if (isChunkUnloadedOrUnpopulated(world, cposX + 1, cposZ))
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
            return true;*/
    	for( ChunkKey.Neighbor nb : ChunkKey.NEIGHBORS ) {
    		if( isChunkUnloadedOrUnpopulated(world, nb.getOffset(pos)) )
    			return true;
    	}

        return false;
    }
}

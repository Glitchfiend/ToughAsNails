package toughasnails.util;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import toughasnails.season.ChunkKey;

public class ChunkUtils
{
    public static boolean isChunkUnloadedOrUnpopulated(World world, ChunkPos pos)
    {
        Chunk chunk = world.getChunkProvider().getLoadedChunk(pos.chunkXPos, pos.chunkZPos);
        return chunk == null || !chunk.isTerrainPopulated();
    }

    public static int identifyUnloadedOrUnpopulatedNeighbors(World world, ChunkPos pos)
    {
        int nbMask = 0;
        for (int i = 0; i < ChunkKey.NEIGHBORS.length; i++)
        {
            ChunkKey.Neighbor nb = ChunkKey.NEIGHBORS[i];
            if (isChunkUnloadedOrUnpopulated(world, nb.getOffset(pos)))
            {
                nbMask |= 0x1 << i;
            }
        }

        return nbMask;
    }
}

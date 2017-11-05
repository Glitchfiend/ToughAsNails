package toughasnails.season;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class ChunkKey
{
    public static final Neighbor[] NEIGHBORS = new Neighbor[] { new Neighbor(1, 0, 2), new Neighbor(1, 1, 7), new Neighbor(-1, 0, 0), new Neighbor(-1, 1, 6), new Neighbor(0, 1, 5), new Neighbor(0, -1, 4), new Neighbor(1, -1, 3),
            new Neighbor(-1, -1, 1) };

    private ChunkPos pos;
    private int dimension;

    public ChunkKey(ChunkPos pos, World world)
    {
        this.pos = pos;
        // NOTE: Don't use level name dependency to build the key, otherwise
        // level renaming may impact functionality.
        this.dimension = world.provider.getDimension();
    }

    public ChunkKey(ChunkPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }

    public boolean isAssociatedToWorld(World world)
    {
        if (world.isRemote)
            return false;
        if (world.provider.getDimension() != dimension)
            return false;
        return true;
    }

    public ChunkPos getPos()
    {
        return pos;
    }

    public int getDimension()
    {
        return dimension;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + dimension;
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChunkKey other = (ChunkKey) obj;
        if (dimension != other.dimension)
            return false;
        if (pos == null)
        {
            if (other.pos != null)
                return false;
        }
        else if (!pos.equals(other.pos))
            return false;
        return true;
    }

    public static class Neighbor
    {
        private final int dX;
        private final int dZ;
        private final int oppositeIdx;

        private Neighbor(int dX, int dZ, int oppositeIdx)
        {
            this.dX = dX;
            this.dZ = dZ;
            this.oppositeIdx = oppositeIdx;
        }

        public int getDX()
        {
            return dX;
        }

        public int getDZ()
        {
            return dZ;
        }

        public int getOppositeIdx()
        {
            return oppositeIdx;
        }

        public ChunkPos getOffset(ChunkPos pos)
        {
            return new ChunkPos(pos.chunkXPos + dX, pos.chunkZPos + dZ);
        }

        public ChunkKey getOffset(ChunkKey key)
        {
            return new ChunkKey(getOffset(key.getPos()), key.getDimension());
        }
    }
}
package toughasnails.season;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class ChunkKey
{
	public static final Neighbor[] NEIGHBORS = new Neighbor[]
	{
		new Neighbor(1,0),
		new Neighbor(1,1),
		new Neighbor(-1,0),
		new Neighbor(-1,1),
		new Neighbor(0,1),
		new Neighbor(0,-1),
		new Neighbor(1,-1),
		new Neighbor(-1,-1)
	};
	
    private ChunkPos pos;
    private int dimension;

    public ChunkKey(ChunkPos pos, World world)
    {
        this.pos = pos;
        // NOTE: Don't use level name dependency to build the key, otherwise level renaming may impact functionality.
        this.dimension = world.provider.getDimension();
    }

    public ChunkKey(ChunkPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
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
    	
    	private Neighbor(int dX, int dZ) {
    		this.dX = dX;
    		this.dZ = dZ;
    	}
    	
    	public int getDX() {
    		return dX;
    	}
    	
    	public int getDZ() {
    		return dZ;
    	}
    	
    	public ChunkPos getOffset( ChunkPos pos ) {
    		return new ChunkPos( pos.chunkXPos + dX, pos.chunkZPos + dZ );
    	}
    	
    	public ChunkKey getOffset( ChunkKey key ) {
    		return new ChunkKey(getOffset(key.getPos()), key.getDimension());
    	}
    }
}
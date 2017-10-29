package toughasnails.season;

import net.minecraft.world.chunk.Chunk;

public class ChunkData
{
    private final ChunkKey key;
    private Chunk chunk;

    private long lastPatchedTime;
    private boolean isToBePatched;
    private ActiveChunkData belongingAC;

    public ChunkData(ChunkKey key, Chunk chunk, long lastPatchedTime)
    {
        this.key = key;
        this.chunk = chunk;
        this.lastPatchedTime = lastPatchedTime;
        this.isToBePatched = false;
        this.belongingAC = null;
    }

    public void setToBePatched(boolean bToBePatched)
    {
        this.isToBePatched = bToBePatched;
    }
    
    public void setBelongingAC(ActiveChunkData belongingAC)
    {
    	this.belongingAC = belongingAC;
    }

    public void setLoadedChunk(Chunk chunk)
    {
        if (chunk == null)
            throw new IllegalArgumentException("chunk must be non null. Use clearLoadedChunk() for other case.");
        this.chunk = chunk;
    }

    public boolean getIsToBePatched()
    {
        return isToBePatched;
    }
    
    public ActiveChunkData getBelongingAC() {
    	return belongingAC;
    }

    public ChunkKey getKey()
    {
        return key;
    }

    public Chunk getChunk()
    {
        return chunk;
    }

    public void setPatchTimeUptodate()
    {
        if (chunk != null)
            this.lastPatchedTime = chunk.getWorld().getTotalWorldTime();
    }

    public void setPatchTimeTo(long lastPatchedTime)
    {
        this.lastPatchedTime = lastPatchedTime;
    }

    public long getLastPatchedTime()
    {
        return lastPatchedTime;
    }

    public void clearLoadedChunk()
    {
        setToBePatched(false);
        this.chunk = null;
    }

}

package toughasnails.season;

import net.minecraft.world.chunk.Chunk;

public class ChunkData
{
    private final ChunkKey key;
    private Chunk chunk;

    private long lastPatchedTime;
    private boolean isVisited;
    private boolean isActivelyUpdated;
    private boolean isToBePatched;

    public ChunkData(ChunkKey key, Chunk chunk, long lastPatchedTime)
    {
        this.key = key;
        this.chunk = chunk;
        this.lastPatchedTime = lastPatchedTime;
        this.isVisited = false;
        this.isActivelyUpdated = false;
        this.isToBePatched = false;
    }

    public void setToBePatched(boolean bToBePatched)
    {
        this.isToBePatched = bToBePatched;
    }

    public void setVisitedFlag(boolean bIsVisited)
    {
        this.isVisited = bIsVisited;
    }

    public void setLoadedChunk(Chunk chunk)
    {
        if (chunk == null)
            throw new IllegalArgumentException("chunk must be non null. Use clearLoadedChunk() for other case.");
        this.chunk = chunk;
    }

    public boolean getIsVisited()
    {
        return isVisited;
    }

    public void setActivelyUpdatedFlag(boolean bIsActive)
    {
        this.isActivelyUpdated = bIsActive;
    }

    public boolean getIsToBePatched()
    {
        return isToBePatched;
    }

    public boolean getIsActivelyUpdated()
    {
        return isActivelyUpdated;
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

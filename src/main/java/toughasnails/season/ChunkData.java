package toughasnails.season;

import net.minecraft.world.chunk.Chunk;

public class ChunkData {
	private final ChunkKey key;
	private Chunk chunk;
	
	private long lastPatchedTime;
	private boolean bIsVisited;
	private boolean bIsActive;
	private boolean bToBePatched;
	
	public ChunkData(ChunkKey key, Chunk chunk, long lastPatchedTime) {
		this.key = key;
		this.chunk = chunk;
		this.lastPatchedTime = lastPatchedTime;
		this.bIsVisited = false;
		this.bIsActive = false;
		this.bToBePatched = false;
	}

	public void setToBePatched(boolean bToBePatched) {
		this.bToBePatched = bToBePatched;
	}

	public void setVisitedFlag( boolean bIsVisited ) {
		this.bIsVisited = bIsVisited;
	}

	public void setLoadedChunk( Chunk chunk ) {
		if( chunk == null )
			throw new IllegalArgumentException("chunk must be non null. Use clearLoadedChunk() for other case.");
		this.chunk = chunk;
	}
	
	public boolean isVisited() {
		return bIsVisited;
	}
	
	public void setActivelyUpdatedFlag(boolean bIsActive) {
		this.bIsActive = bIsActive;
	}
	
	public boolean isToBePatched() {
		return bToBePatched;
	}
	
	public boolean isActivelyUpdated() {
		return bIsActive;
	}
	
	public ChunkKey getKey() {
		return key;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public void setPatchTimeUptodate() {
		if( chunk != null )
			this.lastPatchedTime = chunk.getWorld().getTotalWorldTime();
	}
	
	public void setPatchTimeTo( long lastPatchedTime ) {
		this.lastPatchedTime = lastPatchedTime;
	}

	public long getLastPatchedTime() {
		return lastPatchedTime;
	}

	public void clearLoadedChunk() {
		setToBePatched(false);
		this.chunk = null;
	}

}

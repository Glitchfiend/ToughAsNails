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
		this.chunk = chunk;
	}
	
	public boolean isVisited() {
		return bIsVisited;
	}
	
	public void setActiveFlag(boolean bIsActive) {
		this.bIsActive = bIsActive;
	}
	
	public boolean isToBePatched() {
		return bToBePatched;
	}
	
	public boolean isActive() {
		return bIsActive;
	}
	
	public ChunkKey getKey() {
		return key;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public void setPatchTimeUptodate() {
		this.lastPatchedTime = chunk.getWorld().getTotalWorldTime();
	}

	public long getLastPatchedTime() {
		return lastPatchedTime;
	}

}

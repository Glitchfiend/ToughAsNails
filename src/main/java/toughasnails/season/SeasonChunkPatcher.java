package toughasnails.season;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.core.ToughAsNails;
import toughasnails.handler.season.SeasonHandler;
import toughasnails.util.ChunkUtils;

public class SeasonChunkPatcher {
	
	private static final int PATCHES_PER_TICK = 30;
	private static final int THR_PROB_MAX = 1000;
	private static final long RETROSPECTIVE_WINDOW_TICKS = 24000 * 9;
	
	private Object chunkLock = new Object();
	public Map<ChunkKey, Chunk> pendingChunks = new HashMap<ChunkKey, Chunk>();		// Secured by multithreading access
	
	public SeasonChunkPatcher() {
	}

	public void enqueueChunkOnce(Chunk chunk) {
		synchronized(chunkLock) {
			ChunkKey key = new ChunkKey(chunk.getPos(), chunk.getWorld());
			if( pendingChunks.containsKey(key) )
				return;
			pendingChunks.put(key, chunk);
		}
	}
	
	public void removeChunkIfEnqueued(Chunk chunk) {
		synchronized(chunkLock) {
			ChunkKey key = new ChunkKey(chunk.getPos(), chunk.getWorld());
			pendingChunks.remove(key);
		}
	}
	
	private void addChunkIfGenerated(World world, int cposX, int cposZ) {
		if( !world.isChunkGeneratedAt(cposX, cposZ) )
			return;
		Chunk chunk = world.getChunkFromChunkCoords(cposX, cposZ);
	
		enqueueChunkOnce(chunk);
	}
	
	public void enqueueGeneratedNeighborChunks(World world, int cposX, int cposZ) {
		addChunkIfGenerated(world, cposX + 1, cposZ );
		addChunkIfGenerated(world, cposX + 1, cposZ + 1 );
		addChunkIfGenerated(world, cposX - 1, cposZ );
		addChunkIfGenerated(world, cposX - 1, cposZ + 1 );
		addChunkIfGenerated(world, cposX, cposZ + 1 );
		addChunkIfGenerated(world, cposX, cposZ - 1 );
		addChunkIfGenerated(world, cposX + 1, cposZ - 1 );
		addChunkIfGenerated(world, cposX - 1, cposZ - 1 );
	}
	
	public void onServerWorldTick(WorldServer world) {
		world.profiler.startSection("seasonChunkFind");
		
		ChunkProviderServer provider = world.getChunkProvider();
		SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
		
		// Iterate through actively updated chunks
		Iterator<Chunk> iter = world.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator());
		while(iter.hasNext()) {
			Chunk activeChunk = iter.next();
			ChunkData chunkData = seasonData.getStoredChunkData(activeChunk, true);
			
			if( !chunkData.isActive() ) {
				// Roll up patches
				enqueueChunkOnce(activeChunk);
				
				// Tag as active and as awaiting to be patched
				chunkData.setToBePatched(true);
				chunkData.setActiveFlag(true);
			}
			else if( !chunkData.isToBePatched() ) {
				// For an active chunk (having no pending patching) the time is always actual
				chunkData.setPatchTimeUptodate();
			}
			
			chunkData.setVisitedFlag(true);
		}
		
		// Iterate through loaded chunks to find deactivated chunks
		for( Chunk loadedChunk : provider.getLoadedChunks() ) {
			assert world == loadedChunk.getWorld();
			ChunkData chunkData = seasonData.getStoredChunkData(loadedChunk, false);
			if( chunkData == null )
				continue;	// Untracked chunks aren't active anyway, otherwise they would have been tracked in the first iteration.
			if( chunkData.isVisited() )
				continue;
			
			// This one is not active anymore
			if( chunkData.isActive() )
			{
				chunkData.setActiveFlag(false);
			}
			chunkData.setVisitedFlag(true);
		}
		
		// Remove dead entries
		Iterator<Map.Entry<ChunkKey, ChunkData>> entryIter = seasonData.managedChunks.entrySet().iterator();
		while( entryIter.hasNext() ) {
			ChunkData inactiveChunkData = entryIter.next().getValue();
			Chunk chunk = inactiveChunkData.getChunk();
			if( chunk == null )
				continue;
			if( chunk.getWorld() != world )
				continue;
			if( !inactiveChunkData.isVisited() ) {
				inactiveChunkData.setLoadedChunk(null);
			}
			else {
				inactiveChunkData.setVisitedFlag(false);
			}
		}
		
		world.profiler.endSection();
	}
	
	private void removeWorldFrom( World world, Map<ChunkKey, Chunk> pending ) {
		Iterator<Map.Entry<ChunkKey, Chunk>> iter = pending.entrySet().iterator();
		while( iter.hasNext() ) {

			Map.Entry<ChunkKey, Chunk> entry = iter.next();
			Chunk chunk = entry.getValue();
			if( chunk.getWorld() == world ) {
				iter.remove();
			}
		}
	}
	
	public void onServerWorldUnload(World world) {
		// Clear loadedChunkQueue
		synchronized(chunkLock) {
			removeWorldFrom(world, pendingChunks);
		}
	}
	
	public void onServerTick() {
		Map<ChunkKey, Chunk> chunksInProcess = pendingChunks;
		synchronized(chunkLock) {
			pendingChunks = new HashMap<ChunkKey, Chunk>();	
		}

		int numProcessed = 0;
		Iterator<Map.Entry<ChunkKey, Chunk>> iter = chunksInProcess.entrySet().iterator();
//		for( Chunk chunk : chunksInProcess.values() )
		while(iter.hasNext())
		{
			if( numProcessed++ >= PATCHES_PER_TICK )
				break;
			
			Chunk chunk = iter.next().getValue();
			iter.remove();
			
			if( chunk.unloaded ) {
				ToughAsNails.logger.warn("Unloaded chunk awaiting patch found.");
				continue;
			}
			SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(chunk.getWorld());
			ChunkData chunkData = seasonData.getStoredChunkData(chunk, true);
			ChunkPos chunkPos = chunk.getPos();
			World world = chunk.getWorld(); 
			if( ChunkUtils.hasUnpopulatedNeighbor(world, chunkPos.chunkXPos, chunkPos.chunkZPos) ) {
				chunkData.setActiveFlag(false);
				chunkData.setToBePatched(false);
				continue;
			}
			
			// Perform a chunk patch
			patchChunkTerrain(chunkData);
			
			// Clear to-be-patched flag
			chunkData.setToBePatched(false);
		}
		
		// reinsert unprocessed entries to queue
		if( chunksInProcess.size() > 0 ) {
			synchronized(chunkLock) {
				chunksInProcess.putAll(pendingChunks);
				pendingChunks = chunksInProcess;
			}
		}
	}
	
	private void executePatchCommand( int command, int threshold, Chunk chunk, Season season ) {
		// TODO: Handle client notification on block changes properly!
		
		ChunkPos chunkPos = chunk.getPos();
		World world = chunk.getWorld(); 
		
		if( command == 4 || command == 5 ) {
			threshold = THR_PROB_MAX;
		}

		MutableBlockPos pos = new MutableBlockPos();
		for( int iX = 0; iX < 16; iX ++ ) {
			for( int iZ = 0; iZ < 16; iZ ++ ) {
				int height = chunk.getHeightValue(iX, iZ);
				pos.setPos(chunkPos.getXStart() + iX, height, chunkPos.getZStart() + iZ);
				
		        BlockPos below = pos.down();
		        
		        if( (command == 1 || command == 2 || command == 4) ) {
		        	// Apply snow in dependence of last rain time and apply ice in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned false before).
		        	if( world.rand.nextInt(THR_PROB_MAX) < threshold ) {
		        		
		        		if( SeasonASMHelper.canBlockFreezeInSeason(world, below, false, Season.WINTER) ) {
		        			// NOTE: Is a simplified freeze behavior
		        			world.setBlockState(below, Blocks.ICE.getDefaultState(), 2);
		        		}
		        		else if( command != 1 && SeasonASMHelper.canSnowAtInSeason(world, pos, true, Season.WINTER) ) {
							world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState(), 2);
						}
					}
		        	// TODO: Simulate crop death
				}
		        else if( command == 3 || command == 5 ) {
		        	// Remove snow and ice in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned true before).
		        	if( world.rand.nextInt(THR_PROB_MAX) <= threshold * 10 ) {
			        	IBlockState blockState = world.getBlockState(pos);
			        	if( blockState.getBlock() == Blocks.SNOW_LAYER ) {
			        		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			        	}
			        	else {
			        		blockState = world.getBlockState(below);
			        		if( blockState.getBlock() == Blocks.ICE ) {
			        			world.setBlockState(below, Blocks.WATER.getDefaultState(), 2);
			        			world.neighborChanged(below, Blocks.WATER, below);
			        		}
			        	}			        	
			        }
		        }
			}
		}
	}
	
	private void executePatchCommand( int command, long snowyTrackTicks, long rainingTrackTicks, Chunk chunk, Season season ) {
		if( command != 0 ) {
			int threshold = 0;
			if( command == 2 ) {
				long dur = rainingTrackTicks;
				if( dur > snowyTrackTicks )
					dur = snowyTrackTicks;
				threshold = evalProbUpdateTick((int)dur);
			}
			else if( command == 1 || command == 3 )
				threshold = evalProbUpdateTick((int)snowyTrackTicks);
			executePatchCommand( command, threshold, chunk, season );
		}
	}
	
	private void patchChunkTerrain(ChunkData chunkData) {
		Chunk chunk = chunkData.getChunk();
		World world = chunk.getWorld(); 

		Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
		SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);

		long lastPatchedTime = chunkData.getLastPatchedTime();
		boolean bFastForward = false;
		long windowBorder = world.getTotalWorldTime() - RETROSPECTIVE_WINDOW_TICKS;
		if( lastPatchedTime < windowBorder ) {
			// Old entries have no effect. Considering it by reseting chunk snow states and patch from newer journal entries only
			lastPatchedTime = windowBorder;
			bFastForward = true;
		}
		int fromIdx = seasonData.getJournalIndexAfterTime(lastPatchedTime);

		// determine initial state
		boolean bWasRaining = seasonData.wasLastRaining(fromIdx);
		boolean bWasSnowy = seasonData.wasLastSnowy(fromIdx);
		
		long rainingTrackTicks = 0;
		long snowyTrackTicks = 0;
		
		long intervalRainingTrackStart = lastPatchedTime;
		long intervalSnowyTrackStart = lastPatchedTime;

		// initialize in case of fast forward
		if( bFastForward ) {
			if( bWasSnowy )
				executePatchCommand( 4, 0, chunk, season );
			else
				executePatchCommand( 5, 0, chunk, season );
		}
		
		// Replay latest journal entries
		if( fromIdx != -1 ) {
			int command = 0;	// 0 = NOP. 1 = simulate freeze only. 2 = simulate chunk snow (requires ticks) 3 = simulate melting (requires ticks) 4 = set all snow 5 = set all molten

			// Apply events from journal
			for( int curEntry = fromIdx; curEntry < seasonData.journal.size(); curEntry ++ ) {
				WeatherJournalEvent wevt = seasonData.journal.get(curEntry);

				rainingTrackTicks = wevt.getTimeStamp() - intervalRainingTrackStart;
				snowyTrackTicks = wevt.getTimeStamp() - intervalSnowyTrackStart;

				switch( wevt.getEventType() ) {
				case eventStartRaining:
					if( !bWasRaining ) {
						intervalRainingTrackStart = wevt.getTimeStamp();
						command = 0;
						bWasRaining = true;
					}
					break;				
				case eventStopRaining:
					if( bWasRaining ) {
						intervalRainingTrackStart = wevt.getTimeStamp();
						if( bWasSnowy )
							command = 2;
						else
							command = 0;
						bWasRaining = false;
					}
					break;
				case eventToSnowy:
					if( !bWasSnowy ) {
						intervalSnowyTrackStart = wevt.getTimeStamp();
						command = 3;
						bWasSnowy = true;
					}
					break;
				case eventToNonSnowy:
					if( bWasSnowy ) {
						intervalSnowyTrackStart = wevt.getTimeStamp();
						if( bWasRaining )
							command = 2;
						else
							command = 1;
						bWasSnowy = false;
					}
					break;
				default:
					// Do nothing
					command = 0;
				}

				executePatchCommand( command, snowyTrackTicks, rainingTrackTicks, chunk, season );
			}
		}
		
		// Post update for running events
		rainingTrackTicks = world.getTotalWorldTime() - intervalRainingTrackStart;
		snowyTrackTicks = world.getTotalWorldTime() - intervalSnowyTrackStart;
		
		if( seasonData.wasLastRaining(-1) && seasonData.wasLastSnowy(-1) ) {
			executePatchCommand( 2, snowyTrackTicks, rainingTrackTicks, chunk, season );
		}
		else if( !seasonData.wasLastSnowy(-1) ) {
			executePatchCommand( 3, snowyTrackTicks, rainingTrackTicks, chunk, season );
		}
		else {
			executePatchCommand( 1, snowyTrackTicks, rainingTrackTicks, chunk, season );
		}
		
		chunkData.setPatchTimeUptodate();
	}
	
	private int evalProbUpdateTick(int duringTicks) {
		final double fieldHitProb = 1.0 / (16.0 * 16.0);
		final double snowUpdateProbInTick = 1.0 / 16.0;
		final double correctionFactor = 0.75;
		final double hitProb = correctionFactor * fieldHitProb * snowUpdateProbInTick;
		final double missProb = 1.0 - hitProb;
		double prob = hitProb * (1.0 - Math.pow(missProb, duringTicks + 1)) / (1.0 - missProb);
		
		return (int)((double)THR_PROB_MAX * prob + 0.5);	
	}
}

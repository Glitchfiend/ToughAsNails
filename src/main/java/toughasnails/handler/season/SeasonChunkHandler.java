package toughasnails.handler.season;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.season.SeasonSavedData;
import toughasnails.season.WeatherEventType;
import toughasnails.season.WeatherJournalEvent;

public class SeasonChunkHandler {
	
	private static final int THR_PROB_MAX = 100;
	
	// TODO: Move it!
	public Set<ChunkKey> loadedChunkMask = new HashSet<ChunkKey>();
	public LinkedList<ChunkData> loadedChunkQueue = new LinkedList<ChunkData>();
	
//	public HashMap<ChunkKey, ChunkData> inactiveChunks = new HashMap<ChunkKey, ChunkData>();
	public HashMap<ChunkKey, ChunkData> managedChunks = new HashMap<ChunkKey, ChunkData>();

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		if( event.getWorld().isRemote )
			return;
		
		Chunk chunk = event.getChunk();
		synchronized( loadedChunkQueue ) {
			if( chunk.isTerrainPopulated() ) {
				enqueueChunkOnce(chunk /*, chunk.lastSaveTime */);
			}
		}
	}
	
/*	private boolean isTaggedAsInactive(Chunk chunk) {
		ChunkPos cpos = chunk.getPos();
		ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
		return inactiveChunks.containsKey(key);
	}
	
	private boolean visitInactive(Chunk chunk) {
		ChunkPos cpos = chunk.getPos();
		ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
		ChunkData chunkData = inactiveChunks.get(key);
		if( chunkData == null )
			return false;
		
		chunkData.setVisited();
		return true;
	}*/
	
/*	private boolean isWatchedByAnyPlayer(Chunk chunk) {
		if( chunk.getWorld().isRemote )
			return true;
		WorldServer world = (WorldServer)chunk.getWorld();
		ChunkPos pos = chunk.getPos();
		PlayerChunkMap chunkMap = world.getPlayerChunkMap();
		for( EntityPlayer player : world.playerEntities ) {
			if( !(player instanceof EntityPlayerMP) )
				continue;
			if( chunkMap.isPlayerWatchingChunk((EntityPlayerMP) player, pos.chunkXPos, pos.chunkZPos) )
				return true;
		}
		
		return false;
	} */
	
	private void enqueueChunkOnce(Chunk chunk /*, long lastSaveTime */) {
		ChunkData chunkData = getStoredChunkData(chunk, true);
		ChunkKey key = chunkData.getKey();
		if( loadedChunkMask.contains(key) )
			return;
		loadedChunkMask.add(key);
		loadedChunkQueue.add(chunkData);
/*		ChunkPos cpos = chunk.getPos(); 
		ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
		if( loadedChunkMask.contains(key) )
			return;
		loadedChunkMask.add(key);
		loadedChunkQueue.add(new ChunkData(key, chunk, lastSaveTime)); */
	}
	
/*	private void registerChunkData(ChunkData chunkData) {
		ChunkKey key = chunkData.getKey();
		managedChunks.put(key, chunkData);
	} */
	
	private ChunkData getStoredChunkData(Chunk chunk, boolean bCreateIfNotExisting) {
		ChunkPos cpos = chunk.getPos(); 
		ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
		ChunkData chunkData = managedChunks.get(key);
		if( chunkData != null )
			return chunkData;
		if( !bCreateIfNotExisting )
			return null;
		
		// TODO: Retrieve patch time
		long lastPatchTime = chunk.getWorld().getTotalWorldTime();
		
		chunkData = new ChunkData(key, chunk, lastPatchTime);
		managedChunks.put(key, chunkData);
		return chunkData;
	}
	
/*	private void tagChunkAsInactive(ChunkData chunkData) {
		ChunkKey key = chunkData.getKey();
//		if( inactiveChunks.containsKey(key) )
//			return;
		inactiveChunks.put(key, chunkData);
	} */
	
/*	private ChunkData untagChunkBeingInactive(Chunk chunk) {
		ChunkPos cpos = chunk.getPos(); 
		ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
		
		ChunkData chunkData = inactiveChunks.get(key);
		inactiveChunks.remove(key);
		return chunkData;
	}*/
	
	private boolean isChunkUnpopulated(World world, int cposX, int cposZ ) {
		return !world.isChunkGeneratedAt(cposX, cposZ) || !world.getChunkFromChunkCoords(cposX, cposZ).isTerrainPopulated();
	}
	
	private boolean hasUnpopulatedNeighbor(World world, int cposX, int cposZ) {
		if( isChunkUnpopulated(world, cposX + 1, cposZ ) )
			return true;		
		if( isChunkUnpopulated(world, cposX + 1, cposZ + 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cposX - 1, cposZ ) )
			return true;		
		if( isChunkUnpopulated(world, cposX - 1, cposZ + 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cposX, cposZ + 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cposX, cposZ - 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cposX + 1, cposZ - 1 ) )
			return true;
		if( isChunkUnpopulated(world, cposX - 1, cposZ - 1 ) )
			return true;		

		return false;
	}
	
	private void addChunkIfGenerated(World world, int cposX, int cposZ) {
		if( !world.isChunkGeneratedAt(cposX, cposZ) )
			return;
		Chunk chunk = world.getChunkFromChunkCoords(cposX, cposZ);
	
/*		long lastSaveTime;
		if( chunk.isTerrainPopulated() )
			lastSaveTime = chunk.lastSaveTime;
		else
			lastSaveTime = world.getTotalWorldTime(); */
		
		enqueueChunkOnce(chunk /*, lastSaveTime */);
	}
	
	private void addNeighborChunks(World world, int cposX, int cposZ) {
		addChunkIfGenerated(world, cposX + 1, cposZ );
		addChunkIfGenerated(world, cposX + 1, cposZ + 1 );
		addChunkIfGenerated(world, cposX - 1, cposZ );
		addChunkIfGenerated(world, cposX - 1, cposZ + 1 );
		addChunkIfGenerated(world, cposX, cposZ + 1 );
		addChunkIfGenerated(world, cposX, cposZ - 1 );
		addChunkIfGenerated(world, cposX + 1, cposZ - 1 );
		addChunkIfGenerated(world, cposX - 1, cposZ - 1 );
	}
	
	private void executePatchCommand( int command, int threshold, Chunk chunk, Season season ) {
		// TODO: Handle client notification on block changes properly!
		
		ChunkPos chunkPos = chunk.getPos();
		World world = chunk.getWorld(); 
		
		if( command == 1 ) {
			threshold = THR_PROB_MAX;
		}

		MutableBlockPos pos = new MutableBlockPos();
		for( int iX = 0; iX < 16; iX ++ ) {
			for( int iZ = 0; iZ < 16; iZ ++ ) {
				int height = chunk.getHeightValue(iX, iZ);
				pos.setPos(chunkPos.getXStart() + iX, height, chunkPos.getZStart() + iZ);
				
		        Biome biome = world.getBiome(pos);
		        float temperature = biome.getFloatTemperature(pos);
		        
		        if( (command == 2 || command == 1) && SeasonHelper.canSnowAtTempInSeason(season, temperature) ) {
		        	// TODO: Apply snow in dependence of last rain time.
		        	if( world.rand.nextInt(THR_PROB_MAX) < threshold ) {
						if( world.canSnowAt(pos, true) ) {
							world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState(), 2);
						}
					}
					
					// TODO: Apply ice in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned false before).
				}
		        else if( command == 3 || command == 1 ) {
		        	// TODO: Remove snow in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned true before).
		        	if( world.rand.nextInt(THR_PROB_MAX) <= threshold ) {
			        	IBlockState blockState = world.getBlockState(pos);
			        	if( blockState.getBlock() == Blocks.SNOW_LAYER ) {
			        		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			        	}				        	
			        }
		        	
		        	// TODO: Apply ice melting
		        }
			}
		}
	}
	
	private void patchChunkTerrain(ChunkData chunkData) {
		Chunk chunk = chunkData.getChunk();
		World world = chunk.getWorld(); 

		Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
		SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);

		long lastPatchedTime = chunkData.getLastPatchedTime();
		// TODO: Old entries have no effect. Consider it by reseting chunk states and patch from newer journal entries
		boolean bFastForward = false;
		
		// Replay latest journal entries
		int fromIdx = seasonData.getJournalIndexAfterTime(lastPatchedTime);
		if( fromIdx != -1 ) {
			int command = 0;	// 0 = NOP. 1 = reset chunk state. 2 = simulate chunk snow (requires ticks) 3 = simulate melting (requires ticks) 

			// determine initial state
			boolean bWasRaining = seasonData.wasLastRaining(fromIdx);
			boolean bWasSnowy = seasonData.wasLastSnowy(fromIdx);
			
			long rainingTrackTicks = 0;
			long snowyTrackTicks = 0;
			
			long intervalRainingTrackStart = lastPatchedTime;
			long intervalSnowyTrackStart = lastPatchedTime;

			// initialize in case of fast forward
			if( bFastForward ) {
				executePatchCommand( 1, 0, chunk, season );
			}
			
			// Apply events from journal
			for( int curEntry = fromIdx; curEntry <= seasonData.journal.size(); curEntry ++ ) {
				WeatherJournalEvent wevt;
				if( curEntry < seasonData.journal.size() )
					wevt = seasonData.journal.get(curEntry);
				else {
					// TODO: handle curEntry == -1 case for snowing and melting
					
					WeatherEventType type = WeatherEventType.eventUnknown;
					if( seasonData.wasLastRaining(-1) && seasonData.wasLastSnowy(-1) )
						type = WeatherEventType.eventStopRaining;	// Force update at the switch logic below.
					else if( seasonData.wasLastSnowy(-1) )
						type = WeatherEventType.eventToNonSnowy;	// Force update at the switch logic below.
					wevt = new WeatherJournalEvent(world.getTotalWorldTime(), type);
				}
				
				switch( wevt.getEventType() ) {
				case eventStartRaining:
					if( !bWasRaining ) {
						rainingTrackTicks = wevt.getTimeStamp() - intervalRainingTrackStart;
						snowyTrackTicks = wevt.getTimeStamp() - intervalSnowyTrackStart;
						intervalRainingTrackStart = wevt.getTimeStamp();
//						if( !bWasSnowy )
//							command = 3;
//						else
							command = 0;
						bWasRaining = true;
					}
					break;				
				case eventStopRaining:
					if( bWasRaining ) {
						rainingTrackTicks = wevt.getTimeStamp() - intervalRainingTrackStart;
						snowyTrackTicks = wevt.getTimeStamp() - intervalSnowyTrackStart;
						intervalRainingTrackStart = wevt.getTimeStamp();
//						intervalSnowyTrackStart = wevt.getTimeStamp();	// Split interval, we don't want to simulate twice.
						if( bWasSnowy )
							command = 2;
						else
							command = 0;
						bWasRaining = false;
					}
					break;
				case eventToSnowy:
					if( !bWasSnowy ) {
						rainingTrackTicks = wevt.getTimeStamp() - intervalRainingTrackStart;
						snowyTrackTicks = wevt.getTimeStamp() - intervalSnowyTrackStart;
						intervalSnowyTrackStart = wevt.getTimeStamp();
						command = 3;
						bWasSnowy = true;
					}
					break;
				case eventToNonSnowy:
					if( bWasSnowy ) {
						rainingTrackTicks = wevt.getTimeStamp() - intervalRainingTrackStart;
						snowyTrackTicks = wevt.getTimeStamp() - intervalSnowyTrackStart;
						intervalSnowyTrackStart = wevt.getTimeStamp();
						if( bWasRaining )
							command = 2;
						else
							command = 0;
						bWasSnowy = false;
					}
				default:
					// Do nothing
					command = 0;
				}
				
				if( command != 0 ) {
					int threshold = 0;
					if( command == 2 ) {
						long dur = rainingTrackTicks;
						if( dur > snowyTrackTicks )
							dur = snowyTrackTicks;
						threshold = evalProbUpdateTick((int)dur);
					}
					else if( command == 3 )
						threshold = evalProbUpdateTick((int)intervalSnowyTrackStart);
					executePatchCommand( command, threshold, chunk, season );
				}
			}
		}
		
		chunkData.setPatchTimeUptodate();
	}
	
	@SubscribeEvent
	public void postPopulate(PopulateChunkEvent.Post event) {
		World world = event.getWorld();
		if( world.isRemote )
			return;

		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		synchronized( loadedChunkQueue ) {
			enqueueChunkOnce(chunk /*, world.getTotalWorldTime() */);
			addNeighborChunks(world, event.getChunkX(), event.getChunkZ());
		}
	}

	@SubscribeEvent
	public void worldTick(TickEvent.WorldTickEvent event) {
		if( event.side != Side.SERVER )
			return;
		if( !(event.world instanceof WorldServer) )
			return;	// Should never happen. Just for sure.
		WorldServer world = (WorldServer) event.world;
		ChunkProviderServer provider = world.getChunkProvider();
		
		// Iterate through actively updated chunks
		Iterator<Chunk> iter = world.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator());
		while(iter.hasNext()) {
			Chunk activeChunk = iter.next();
			ChunkData chunkData = getStoredChunkData(activeChunk, true);
			
			if( !chunkData.isActive() ) {
				enqueueChunkOnce(activeChunk);
				chunkData.setActiveFlag(true);
				
/*				// Roll up patches
				patchChunkTerrain(chunkData);
				
				// Tag as active
				chunkData.setActiveFlag(true); */
			}
			
			chunkData.setVisitedFlag(true);
		}
		
		// Iterate through loaded chunks to find deactivated chunks
		for( Chunk loadedChunk : provider.getLoadedChunks() ) {
			assert world == loadedChunk.getWorld();
			ChunkData chunkData = getStoredChunkData(loadedChunk, false);
			if( chunkData == null )
				continue;	// Untracked chunks aren't active anyway, otherwise they would have been tracked in the first iteration.
			if( chunkData.isVisited() )
				continue;
			
			// This one is not active anymore
			chunkData.setActiveFlag(false);
			chunkData.setVisitedFlag(true);
		}
		
/*		for( Chunk loadedChunk : provider.getLoadedChunks() ) {
			assert world == loadedChunk.getWorld();
			
			boolean isTaggedAsInactive = visitInactive(loadedChunk);
			boolean isWatched = isWatchedByAnyPlayer(loadedChunk);
			
			if( isTaggedAsInactive && isWatched ) {
				// Untag from being inactive
				ChunkData chunkData = untagChunkBeingInactive(loadedChunk);
				
				// Roll up patches
				patchChunkTerrain(chunkData);
			}
			else if( !isTaggedAsInactive && !isWatched ) {
				// Tag as inactive
				ChunkKey key = new ChunkKey(loadedChunk.getPos(), world);
				ChunkData chunkData = new ChunkData( key, loadedChunk, world.getTotalWorldTime() );
				tagChunkAsInactive(chunkData);
			}
		}
		
		// Remove dead entries
		Iterator<Map.Entry<ChunkKey, ChunkData>> entryIter = inactiveChunks.entrySet().iterator();
		while( entryIter.hasNext() ) {
			ChunkData inactiveChunkData = entryIter.next().getValue();
			Chunk chunk = inactiveChunkData.getChunk();
			if( chunk.getWorld() != world )
				continue;
			if( !inactiveChunkData.isVisited() ) {
				entryIter.remove();
			}
			else {
				inactiveChunkData.clearVisited();
			}
		}*/
		
		// Remove dead entries
		Iterator<Map.Entry<ChunkKey, ChunkData>> entryIter = managedChunks.entrySet().iterator();
		while( entryIter.hasNext() ) {
			ChunkData inactiveChunkData = entryIter.next().getValue();
			Chunk chunk = inactiveChunkData.getChunk();
			if( chunk.getWorld() != world )
				continue;
			if( !inactiveChunkData.isVisited() ) {
				// TODO: Persist lastPatchedTime to chunk data
				entryIter.remove();
			}
			else {
				inactiveChunkData.setVisitedFlag(false);
			}
		}
	}
	
	@SubscribeEvent
	public void worldUnload(WorldEvent.Unload event) {
		World world = event.getWorld();
		if( world.isRemote )
			return;
		
		// Clear loadedChunkQueue
		Iterator<ChunkData> listIter = loadedChunkQueue.iterator();
		while( listIter.hasNext() ) {
			ChunkData chunkData = listIter.next();
			Chunk chunk = chunkData.getChunk();
			if( chunk.getWorld() == world ) {
				listIter.remove();
				loadedChunkMask.remove(chunkData.getKey());
			}
		}
		
/*		// Clear inactive chunk tags
		Iterator<Map.Entry<ChunkKey, ChunkData>> entryIter = inactiveChunks.entrySet().iterator();
		while( entryIter.hasNext() ) {
			ChunkData inactiveChunkData = entryIter.next().getValue();
			Chunk chunk = inactiveChunkData.getChunk();
			if( chunk.getWorld() == world ) {
				entryIter.remove();
			}
		} */
		
		// Clear managed chunk tags
		Iterator<Map.Entry<ChunkKey, ChunkData>> entryIter = managedChunks.entrySet().iterator();
		while( entryIter.hasNext() ) {
			ChunkData inactiveChunkData = entryIter.next().getValue();
			Chunk chunk = inactiveChunkData.getChunk();
			if( chunk.getWorld() == world ) {
				// TODO: Persist lastPatchedTime to chunk data
				entryIter.remove();
			}
		}
	}
	
	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) {
		synchronized( loadedChunkQueue ) {
			for( int i = 0; i < loadedChunkQueue.size(); i ++ ) {
				ChunkData chunkData = loadedChunkQueue.get(i);
				Chunk chunk = chunkData.getChunk();
				ChunkPos chunkPos = chunk.getPos();
				World world = chunk.getWorld(); 
				if( hasUnpopulatedNeighbor(world, chunkPos.chunkXPos, chunkPos.chunkZPos) )
					continue;
				
				// Perform a chunk patch
				patchChunkTerrain(chunkData);
				
				// Register as active
//				chunkData.setActiveFlag(true);
//				registerChunkData(chunkData);
				
/*				// Initially register as hibernated if it is the case
				boolean isWatched = isWatchedByAnyPlayer(chunk);
				if( isWatched )
					untagChunkBeingInactive(chunk);
				else
					tagChunkAsInactive(chunkData); */
			}
			loadedChunkMask.clear();
			loadedChunkQueue.clear();
		}
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
	
	private static class ChunkKey {
		private ChunkPos pos;
	    private int dimension;
	    private String levelName;
		
		ChunkKey(ChunkPos pos, World world) {
			this.pos = pos;
			this.levelName = world.getWorldInfo().getWorldName();
			this.dimension = world.provider.getDimension();
		}

		public ChunkPos getPos() {
			return pos;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + dimension;
			result = prime * result + ((levelName == null) ? 0 : levelName.hashCode());
			result = prime * result + ((pos == null) ? 0 : pos.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ChunkKey other = (ChunkKey) obj;
			if (dimension != other.dimension)
				return false;
			if (levelName == null) {
				if (other.levelName != null)
					return false;
			} else if (!levelName.equals(other.levelName))
				return false;
			if (pos == null) {
				if (other.pos != null)
					return false;
			} else if (!pos.equals(other.pos))
				return false;
			return true;
		}
	}
	
	private static class ChunkData {
		private final ChunkKey key;
		private final Chunk chunk;
		
		private long lastPatchedTime;
		private boolean bIsVisited;
		private boolean bIsActive;
		
		ChunkData(ChunkKey key, Chunk chunk, long lastPatchedTime) {
			this.key = key;
			this.chunk = chunk;
			this.lastPatchedTime = lastPatchedTime;
			this.bIsVisited = false;
			this.bIsActive = false;
		}

		public void setVisitedFlag( boolean bIsVisited ) {
			this.bIsVisited = bIsVisited;
		}

		public boolean isVisited() {
			return bIsVisited;
		}
		
		public void setActiveFlag(boolean bIsActive) {
			this.bIsActive = bIsActive;
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
	
}

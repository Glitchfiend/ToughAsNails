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
	
	// TODO: Better multi threading locks or some sort of pipelining. Hopefully no cascading calls ...
	
	// TODO: Move it to a dedicated class!
	public Set<ChunkKey> loadedChunkMask = new HashSet<ChunkKey>();
	public LinkedList<ChunkData> loadedChunkQueue = new LinkedList<ChunkData>();
	
	public HashMap<ChunkKey, ChunkData> managedChunks = new HashMap<ChunkKey, ChunkData>();

	@SubscribeEvent
	public synchronized void chunkLoad(ChunkDataEvent.Load event) {
		if( event.getWorld().isRemote )
			return;
		
		Chunk chunk = event.getChunk();
		if( chunk.isTerrainPopulated() ) {
			enqueueChunkOnce(chunk);
		}
	}
	
	private void enqueueChunkOnce(Chunk chunk) {
		ChunkData chunkData = getStoredChunkData(chunk, true);
		ChunkKey key = chunkData.getKey();
		if( loadedChunkMask.contains(key) )
			return;
		chunkData.setToBePatched(true);
		loadedChunkMask.add(key);
		loadedChunkQueue.add(chunkData);
	}

	private ChunkData getStoredChunkData(Chunk chunk, boolean bCreateIfNotExisting) {
		ChunkPos cpos = chunk.getPos(); 
		ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
		ChunkData chunkData = managedChunks.get(key);
		if( chunkData != null )
			return chunkData;
		if( !bCreateIfNotExisting )
			return null;
		
		// TODO: Retrieve patch time
		long lastPatchTime = 0; // chunk.getWorld().getTotalWorldTime();
		
		chunkData = new ChunkData(key, chunk, lastPatchTime);
		chunkData.setActiveFlag(false);
		managedChunks.put(key, chunkData);
		return chunkData;
	}
	
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
	
		enqueueChunkOnce(chunk);
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
		        
		        if( (command == 2 || command == 1) && SeasonHelper.canSnowAtTempInSeason(Season.WINTER, temperature) ) {
		        	// TODO: Apply snow in dependence of last rain time.
		        	if( world.rand.nextInt(THR_PROB_MAX) < threshold ) {
						if( world.canSnowAt(pos, true) ) {
							world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState(), 2);
						}
					}
					
					// TODO: Apply ice in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned false before).
		        	// TODO: Perform crop hibernation
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
	
	private void executePatchCommand( int command, long snowyTrackTicks, long rainingTrackTicks, Chunk chunk, Season season ) {
		if( command != 0 ) {
			int threshold = 0;
			if( command == 2 ) {
				long dur = rainingTrackTicks;
				if( dur > snowyTrackTicks )
					dur = snowyTrackTicks;
				threshold = evalProbUpdateTick((int)dur);
			}
			else if( command == 3 )
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
		// TODO: Old entries have no effect. Consider it by reseting chunk states and patch from newer journal entries
		boolean bFastForward = false;
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
			executePatchCommand( 1, 0, chunk, season );
		}
		
		// Replay latest journal entries
		if( fromIdx != -1 ) {
			int command = 0;	// 0 = NOP. 1 = reset chunk state. 2 = simulate chunk snow (requires ticks) 3 = simulate melting (requires ticks) 
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
							command = 0;
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
		
		chunkData.setPatchTimeUptodate();
	}
	
	@SubscribeEvent
	public synchronized void postPopulate(PopulateChunkEvent.Post event) {
		World world = event.getWorld();
		if( world.isRemote )
			return;

		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		enqueueChunkOnce(chunk);
		addNeighborChunks(world, event.getChunkX(), event.getChunkZ());
	}

	@SubscribeEvent
	public synchronized void worldTick(TickEvent.WorldTickEvent event) {
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
				// Roll up patches
				enqueueChunkOnce(activeChunk);
				
				// Tag as active
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
			ChunkData chunkData = getStoredChunkData(loadedChunk, false);
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
	public synchronized void worldUnload(WorldEvent.Unload event) {
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
	public synchronized void serverTick(TickEvent.ServerTickEvent event) {
		for( int i = 0; i < loadedChunkQueue.size(); i ++ ) {
			ChunkData chunkData = loadedChunkQueue.get(i);
			Chunk chunk = chunkData.getChunk();
			ChunkPos chunkPos = chunk.getPos();
			World world = chunk.getWorld(); 
			if( hasUnpopulatedNeighbor(world, chunkPos.chunkXPos, chunkPos.chunkZPos) ) {
				chunkData.setActiveFlag(false);
				chunkData.setToBePatched(false);
				continue;
			}
			
			// Perform a chunk patch
			patchChunkTerrain(chunkData);
			
			// Clear to-be-patched flag
			chunkData.setToBePatched(false);
		}
		loadedChunkMask.clear();
		loadedChunkQueue.clear();
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
		private boolean bToBePatched;
		
		ChunkData(ChunkKey key, Chunk chunk, long lastPatchedTime) {
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
	
}

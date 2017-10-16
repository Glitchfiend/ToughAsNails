package toughasnails.handler.season;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.season.SeasonSavedData;

public class SeasonChunkHandler {
	
	// TODO: Move it!
	// TODO: World dependent key!
	public Set<ChunkKey> loadedChunkMask = new HashSet<ChunkKey>();
	public LinkedList<ChunkData> loadedChunkQueue = new LinkedList<ChunkData>();

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		
		// TODO: Don't perform operation on unpopulated chunks
		if( event.getWorld().isRemote )
			return;
		
		Chunk chunk = event.getChunk();
		synchronized( loadedChunkQueue ) {
			if( chunk.isTerrainPopulated() ) {
				enqueueChunkOnce(chunk, chunk.lastSaveTime);
			}
		}
	}
	
	private void enqueueChunkOnce(Chunk chunk, long lastSaveTime) {
		ChunkPos cpos = chunk.getPos(); 
		ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
		if( loadedChunkMask.contains(key) )
			return;
		loadedChunkMask.add(key);
		loadedChunkQueue.add(new ChunkData(chunk, lastSaveTime));
	}
	
	private boolean isChunkUnpopulated(World world, int cposX, int cposZ ) {
		return !world.isChunkGeneratedAt(cposX, cposZ) || !world.getChunkFromChunkCoords(cposX, cposZ).isTerrainPopulated();
	}
	
	private boolean hasUnpopulatedNeighbor(World world, Chunk chunk) {
		ChunkPos cpos = chunk.getPos();
/*		if( isChunkUnpopulated(world, cpos.chunkXPos - 1, cpos.chunkZPos ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos + 1, cpos.chunkZPos ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos, cpos.chunkZPos - 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos, cpos.chunkZPos + 1 ) )
			return true;*/
/*		if( isChunkUnpopulated(world, cpos.chunkXPos + 1, cpos.chunkZPos ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos, cpos.chunkZPos + 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos + 1, cpos.chunkZPos + 1 ) )
			return true;		*/
		
		if( isChunkUnpopulated(world, cpos.chunkXPos + 1, cpos.chunkZPos ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos + 1, cpos.chunkZPos + 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos - 1, cpos.chunkZPos ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos - 1, cpos.chunkZPos + 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos, cpos.chunkZPos + 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos, cpos.chunkZPos - 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos + 1, cpos.chunkZPos - 1 ) )
			return true;
		if( isChunkUnpopulated(world, cpos.chunkXPos - 1, cpos.chunkZPos - 1 ) )
			return true;		

		return false;
	}
	
	private void addChunkIfGenerated(World world, int cposX, int cposZ) {
		if( !world.isChunkGeneratedAt(cposX, cposZ) )
			return;
		Chunk chunk = world.getChunkFromChunkCoords(cposX, cposZ);
//		if( hasUnpopulatedNeighbor(world, chunk) )
//			return;	// TODO: change to cposx, cposz style. Is faster!
		
		long lastSaveTime;
		if( chunk.isTerrainPopulated() )
			lastSaveTime = chunk.lastSaveTime;
		else
			lastSaveTime = world.getTotalWorldTime();
		
		enqueueChunkOnce(chunk, lastSaveTime);
	}
	
	private void addNeighborChunks(World world, int cposX, int cposZ) {
/*		ChunkPos cpos = chunk.getPos();
		addChunkIfGenerated(world, cpos.chunkXPos - 1, cpos.chunkZPos );		
		addChunkIfGenerated(world, cpos.chunkXPos + 1, cpos.chunkZPos );
		addChunkIfGenerated(world, cpos.chunkXPos, cpos.chunkZPos - 1 );		
		addChunkIfGenerated(world, cpos.chunkXPos, cpos.chunkZPos + 1 ); */
		addChunkIfGenerated(world, cposX + 1, cposZ );
		addChunkIfGenerated(world, cposX + 1, cposZ + 1 );
		addChunkIfGenerated(world, cposX - 1, cposZ );
		addChunkIfGenerated(world, cposX - 1, cposZ + 1 );
		addChunkIfGenerated(world, cposX, cposZ + 1 );
		addChunkIfGenerated(world, cposX, cposZ - 1 );
		addChunkIfGenerated(world, cposX + 1, cposZ - 1 );
		addChunkIfGenerated(world, cposX - 1, cposZ - 1 );
	}
	
	@SubscribeEvent
	public void postPopulate(PopulateChunkEvent.Post event) {
		World world = event.getWorld();
		if( world.isRemote )
			return;

		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		synchronized( loadedChunkQueue ) {
			enqueueChunkOnce(chunk, world.getTotalWorldTime());
			addNeighborChunks(world, event.getChunkX(), event.getChunkZ());
		}
	}

	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) {
		synchronized( loadedChunkQueue ) {
			for( int i = 0; i < loadedChunkQueue.size(); i ++ ) {
				ChunkData chunkData = loadedChunkQueue.get(i);
				Chunk chunk = chunkData.getChunk();
				World world = chunk.getWorld(); 
				if( hasUnpopulatedNeighbor(world, chunk) )
					continue;
				
				Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
				SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
				ChunkPos chunkPos = chunk.getPos();
		        
				long lastSaveTime = chunkData.getLastSaveTime();
				long simulationWindowTime = world.getTotalWorldTime() - lastSaveTime;
				if( simulationWindowTime > SeasonSavedData.MAX_RAINWINDOW )
					simulationWindowTime = SeasonSavedData.MAX_RAINWINDOW;
				
				// Dirty and wrong, but quick.
				int snowTicks = seasonData.snowTicks;
				if( snowTicks > simulationWindowTime )
					snowTicks = (int)simulationWindowTime;
				int meltTicks = seasonData.meltTicks;
				if( meltTicks > simulationWindowTime )
					meltTicks = (int)simulationWindowTime;
				
				// Get thresholds
				int snowPileThreshold = evalProbUpdateTick(snowTicks);
				int snowMeltThreshold = evalProbUpdateTick(meltTicks);
				
				// TODO: Get weather changes from history.
				
				MutableBlockPos pos = new MutableBlockPos();
				for( int iX = 0; iX < 16; iX ++ ) {
					for( int iZ = 0; iZ < 16; iZ ++ ) {
						int height = chunk.getHeightValue(iX, iZ);
						pos.setPos(chunkPos.getXStart() + iX, height, chunkPos.getZStart() + iZ);
						
				        Biome biome = world.getBiome(pos);
				        float temperature = biome.getFloatTemperature(pos);
				        
				        if( SeasonHelper.canSnowAtTempInSeason(season, temperature) ) {
				        	// TODO: Apply snow in dependence of last rain time.
				        	if( world.rand.nextInt(100) < snowPileThreshold ) {
								if( world.canSnowAt(pos, true) ) {
									world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState(), 2);
								}
							}
							
							// TODO: Apply ice in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned false before).
						}
				        else {
				        	// TODO: Remove snow in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned true before).
				        	if( world.rand.nextInt(100) <= snowMeltThreshold ) {
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
			loadedChunkMask.clear();
			loadedChunkQueue.clear();
		}
	}
	
	private int evalProbUpdateTick(int duringTicks) {
		final double fieldHitProb = 1.0 / (16.0 * 16.0);
		final double snowUpdateProbInTick = 1.0 / 16.0;
		final double hitProb = fieldHitProb * snowUpdateProbInTick;
		final double missProb = 1.0 - hitProb;
		double prob = hitProb * (1.0 - Math.pow(missProb, duringTicks + 1)) / (1.0 - missProb);
		
		return (int)(100.0 * prob + 0.5);	
	}
	
	private static class ChunkKey {
		private ChunkPos pos;
		
		ChunkKey(ChunkPos pos, World world) {
			this.pos = pos;
			// TODO: Add world id!
		}

		public ChunkPos getPos() {
			return pos;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			if (pos == null) {
				if (other.pos != null)
					return false;
			} else if (!pos.equals(other.pos))
				return false;
			return true;
		}
	}
	
	private static class ChunkData {
		private Chunk chunk;
		private long lastSaveTime;
		
		public Chunk getChunk() {
			return chunk;
		}

		public long getLastSaveTime() {
			return lastSaveTime;
		}

		ChunkData(Chunk chunk, long lastSaveTime) {
			this.chunk = chunk;
			this.lastSaveTime = lastSaveTime;
		}
	}
	
}

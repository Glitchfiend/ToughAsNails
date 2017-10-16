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

public class SeasonChunkHandler {
	
	// TODO: Move it!
	// TODO: World dependent key!
	public Set<ChunkPos> loadedChunkSet = new HashSet<ChunkPos>();
	public LinkedList<Chunk> loadedChunkQueue = new LinkedList<Chunk>();

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event) {
		
		// TODO: Don't perform operation on unpopulated chunks
		if( event.getWorld().isRemote )
			return;
		
		Chunk chunk = event.getChunk();
		synchronized( loadedChunkQueue ) {
			if( chunk.isTerrainPopulated() ) {
				enqueueChunkOnce(chunk);
			}
		}
		
/*		*/
	}
	
	private void enqueueChunkOnce(Chunk chunk) {
		ChunkPos cpos = chunk.getPos(); 
		if( loadedChunkSet.contains(cpos) )
			return;
		loadedChunkSet.add(cpos);
		loadedChunkQueue.add(chunk);
	}
	
	private boolean isChunkUnpopulated(World world, int cposX, int cposZ ) {
		return !world.isChunkGeneratedAt(cposX, cposZ) || !world.getChunkFromChunkCoords(cposX, cposZ).isTerrainPopulated();
	}
	
	private boolean hasUnpopulatedNeighbor(World world, Chunk chunk) {
		ChunkPos cpos = chunk.getPos();
		if( isChunkUnpopulated(world, cpos.chunkXPos - 1, cpos.chunkZPos ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos + 1, cpos.chunkZPos ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos, cpos.chunkZPos - 1 ) )
			return true;		
		if( isChunkUnpopulated(world, cpos.chunkXPos, cpos.chunkZPos + 1 ) )
			return true;
		return false;
	}
	
	private void addChunkIfGenerated(World world, int cposX, int cposZ) {
		if( !world.isChunkGeneratedAt(cposX, cposZ) )
			return;
		Chunk chunk = world.getChunkFromChunkCoords(cposX, cposZ);
//		if( hasUnpopulatedNeighbor(world, chunk) )
//			return;	// TODO: change to cposx, cposz style. Is faster!
		
		enqueueChunkOnce(chunk);
	}
	
	private void addNeighborChunks(World world, Chunk chunk) {
		ChunkPos cpos = chunk.getPos();
		addChunkIfGenerated(world, cpos.chunkXPos - 1, cpos.chunkZPos );		
		addChunkIfGenerated(world, cpos.chunkXPos + 1, cpos.chunkZPos );		
		addChunkIfGenerated(world, cpos.chunkXPos, cpos.chunkZPos - 1 );		
		addChunkIfGenerated(world, cpos.chunkXPos, cpos.chunkZPos + 1 );
	}
	
	@SubscribeEvent
	public void postPopulate(PopulateChunkEvent.Post event) {
		World world = event.getWorld();
		if( world.isRemote )
			return;

		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		synchronized( loadedChunkQueue ) {
			enqueueChunkOnce(chunk);
			addNeighborChunks(world, chunk);
		}
	}

	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) {
		synchronized( loadedChunkQueue ) {
			for( int i = 0; i < loadedChunkQueue.size(); i ++ ) {
				Chunk chunk = loadedChunkQueue.get(i);
				World world = chunk.getWorld(); 
				if( hasUnpopulatedNeighbor(world, chunk) )
					continue;
				
				Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
				ChunkPos chunkPos = chunk.getPos();
		        
				MutableBlockPos pos = new MutableBlockPos();
				for( int iX = 0; iX < 16; iX ++ ) {
					for( int iZ = 0; iZ < 16; iZ ++ ) {
						int height = chunk.getHeightValue(iX, iZ);
						pos.setPos(chunkPos.getXStart() + iX, height, chunkPos.getZStart() + iZ);
						
				        Biome biome = world.getBiome(pos);
				        float temperature = biome.getFloatTemperature(pos);
				        
				        if( SeasonHelper.canSnowAtTempInSeason(season, temperature) ) {
				        	// TODO: Apply snow in dependence of last rain time.
				        	
							if( world.canSnowAt(pos, true) ) {
								world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState(), 2);
							}
							
							// TODO: Apply ice in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned false before).
						}
				        else {
				        	// TODO: Remove snow in dependence of last time the season changed to cold (where canSnowAtTempInSeason have returned true before).

				        	IBlockState blockState = world.getBlockState(pos);
				        	if( blockState.getBlock() == Blocks.SNOW_LAYER ) {
				        		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				        	}
				        	
				        }
					}
				}
			}
			loadedChunkSet.clear();
			loadedChunkQueue.clear();
		}
	}
}

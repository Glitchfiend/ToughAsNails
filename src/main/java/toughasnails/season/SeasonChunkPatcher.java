package toughasnails.season;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season;
import toughasnails.api.season.SeasonHelper;
import toughasnails.handler.season.SeasonHandler;
import toughasnails.util.BinaryHeap;
import toughasnails.util.ChunkUtils;

public class SeasonChunkPatcher
{
//	private static final int INITIAL_QUEUESIZE = 1000;
	
    private static final int THR_PROB_MAX = 1000;
    private static final long RETROSPECTIVE_WINDOW_TICKS = 24000 * 9;

    private int numPatcherPerTick;
    private int awaitTicksBeforeDeactivation;
    
    public int statisticsVisitedActive;
    public int statisticsAddedToActive;
    public int statisticsDeletedFromActive;
    public int statisticsPendingAmount;
    public int statisticsRejectedPendingAmount;

//    private Object chunkLock = new Object();

    /**
     * Secured by multi-threading access
     */
    public HashSet<ChunkKey> pendingChunksMask = new HashSet<ChunkKey>();
    public LinkedList<PendingChunkEntry> pendingChunkList = new LinkedList<PendingChunkEntry>();
    public HashMap<ChunkKey, PendingChunkEntry> waitingChunks = new HashMap<ChunkKey, PendingChunkEntry>();

    public BinaryHeap<Long, ActiveChunkData> updatedChunksQueue = new BinaryHeap<Long, ActiveChunkData>();

    public SeasonChunkPatcher()
    {
        numPatcherPerTick = SyncedConfig.getIntValue(SeasonsOption.NUM_PATCHES_PER_TICK);
        awaitTicksBeforeDeactivation = SyncedConfig.getIntValue(SeasonsOption.PATCH_TICK_DISTANCE);
        
        statisticsVisitedActive = 0;
        statisticsAddedToActive = 0;
        statisticsDeletedFromActive = 0;
        statisticsPendingAmount = 0;
        statisticsRejectedPendingAmount = 0;
    }
    
    public void notifyLoadedAndPopulated(World world, ChunkPos chunkPos) {
    	// TODO: ...
    }
    
    public void enqueueChunkOnce(Chunk chunk)
    {
//        synchronized (chunkLock)
//        {
            ChunkKey key = new ChunkKey(chunk.getPos(), chunk.getWorld());
            if (pendingChunksMask.contains(key))
                return;
            pendingChunksMask.add(key);
            pendingChunkList.add(new PendingChunkEntry(chunk));
//        }
    }
    
    public void enqueueChunkOnce(World world, ChunkPos chunkPos)
    {
//        synchronized (chunkLock)
//        {
	    	ChunkKey key = new ChunkKey(chunkPos, world);
	        if (pendingChunksMask.contains(key))
	            return;
	        pendingChunksMask.add(key);
	        pendingChunkList.add(new PendingChunkEntry(key, world));
//        }
    }

    private void addChunkIfGenerated(World world, ChunkPos pos)
    {
        if (!world.isChunkGeneratedAt(pos.chunkXPos, pos.chunkZPos))
            return;
        enqueueChunkOnce(world, pos);
    }

    public void enqueueGeneratedNeighborChunks(World world, ChunkPos pos)
    {
/*        addChunkIfGenerated(world, cposX + 1, cposZ);
        addChunkIfGenerated(world, cposX + 1, cposZ + 1);
        addChunkIfGenerated(world, cposX - 1, cposZ);
        addChunkIfGenerated(world, cposX - 1, cposZ + 1);
        addChunkIfGenerated(world, cposX, cposZ + 1);
        addChunkIfGenerated(world, cposX, cposZ - 1);
        addChunkIfGenerated(world, cposX + 1, cposZ - 1);
        addChunkIfGenerated(world, cposX - 1, cposZ - 1); */
    	for( ChunkKey.Neighbor nb : ChunkKey.NEIGHBORS ) {
    		addChunkIfGenerated(world, nb.getOffset(pos));
    	}
    }

    public void onServerWorldTick(WorldServer world)
    {
        world.profiler.startSection("seasonChunkFind");

        SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);

        // Iterate through actively updated chunks to enqueue them for patching
        // and begin tracking them
        statisticsVisitedActive = 0;
        statisticsAddedToActive = 0;
        Iterator<Chunk> iter = world.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator());
        while (iter.hasNext())
        {
            Chunk activeChunk = iter.next();
            ChunkData chunkData = seasonData.getStoredChunkData(activeChunk, true);

            ActiveChunkData ac = chunkData.getBelongingAC();
            if (ac == null)
            {
                // Roll up patches
                enqueueChunkOnce(activeChunk);
                chunkData.setToBePatched(true);

                // Tag as active and as awaiting to be patched
                ac = new ActiveChunkData(chunkData, world);
                
                statisticsAddedToActive ++;
            }
            else if (!chunkData.getIsToBePatched())
            {
                // For an active chunk (having no pending patching)
                // the time is always actual
                chunkData.setPatchTimeUptodate();
            }

            updatedChunksQueue.remove(ac);
            ac.setNodeKey(world.getTotalWorldTime());
            updatedChunksQueue.add(ac);

            statisticsVisitedActive ++;
        }

        world.profiler.endSection();
    }

    public void onChunkUnload(Chunk chunk)
    {
    	SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(chunk.getWorld());
        ChunkData data = seasonData.getStoredChunkData(chunk, false);
        if( data != null)
        	internRemoveFromQueue(data);
    }

    public void onServerWorldUnload(World world)
    {
        // Clear loadedChunkQueue
//        synchronized (chunkLock)
//        {
            Iterator<PendingChunkEntry> iter = pendingChunkList.iterator();
            while (iter.hasNext())
            {
            	PendingChunkEntry entry = iter.next();
                if (entry.getWorld() == world)
                {
                	pendingChunksMask.remove(entry.getKey());
                    iter.remove();
                }
            }
//        }

        // Clear active chunk tracking for the world
        LinkedList<ActiveChunkData> chunks = new LinkedList<ActiveChunkData>();
        for (ActiveChunkData ac : updatedChunksQueue)
        {
            if (ac.getWorld() == world)
            	ac.detach();
            else
            	chunks.add(ac);
        }
        
        updatedChunksQueue.clear();
        for( ActiveChunkData ac : chunks ) {
        	updatedChunksQueue.add(ac);
        }
    }

    public void onServerTick()
    {
        LinkedList<PendingChunkEntry> chunksInProcess = pendingChunkList;
//        synchronized (chunkLock)
//        {
            statisticsDeletedFromActive = 0;

        	// Iterate through loaded chunks to untrack non active chunks
        	while( true ) {
        		ActiveChunkData ac = updatedChunksQueue.peek();
        		if( ac == null )
        			break;
        		
                // Wait for discount and then remove
        		World world = ac.getWorld();
                if (ac.getLastVisitTime() + awaitTicksBeforeDeactivation <= world.getTotalWorldTime())
                {
                	ac.detach();
                	updatedChunksQueue.remove(ac);
                	
                	statisticsDeletedFromActive ++;
                }
                else
                	break;
        	}
        	
//        	pendingChunkList = new LinkedList<PendingChunkEntry>();
//        }
        
        statisticsPendingAmount = chunksInProcess.size();
        statisticsRejectedPendingAmount = 0;
        
        int numProcessed = 0;
        for( PendingChunkEntry entry : chunksInProcess)
        {
            if (numProcessed >= numPatcherPerTick)
                break;
            numProcessed ++;
            
            Chunk chunk = entry.getChunk();
            
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(chunk.getWorld());
            ChunkData chunkData = seasonData.getStoredChunkData(chunk, true);
            if (chunk.unloaded)
            {
            	internRemoveFromQueue(chunkData);
            	
            	statisticsRejectedPendingAmount ++;
                continue;
            }
            
            ChunkPos chunkPos = chunk.getPos();
            World world = chunk.getWorld();
            int unavailableChunkMask = ChunkUtils.identifyUnloadedOrUnpopulatedNeighbors(world, chunkPos);
            if (unavailableChunkMask != 0)
            {
            	// TODO: set on waiting and notification list.
            	
            	internRemoveFromQueue(chunkData);
            	
            	statisticsRejectedPendingAmount ++;
                continue;
            }

            // Perform a chunk patch
            patchChunkTerrain(chunkData);

            // Clear to-be-patched flag
            chunkData.setToBePatched(false);
        }
        
        // Remove all processed chunks from list within the lock
//        synchronized (chunkLock)
//        {
        	// First drop all processed chunks
        	for( int i = 0; i < numProcessed; i ++ ) {
        		PendingChunkEntry chunkEntry = chunksInProcess.getFirst();
        		pendingChunksMask.remove(chunkEntry.getKey());
        		chunksInProcess.removeFirst();
        	}
        	
        	// reinsert unprocessed entries to queue
/*        	if (chunksInProcess.size() > 0)
        	{
        		chunksInProcess.addAll(pendingChunkList);
        		pendingChunkList = chunksInProcess;
        	} */
//    	}
    }
    
    private void internRemoveFromQueue(ChunkData chunkData) {
        ActiveChunkData ac = chunkData.getBelongingAC();
        if( ac != null )
        {
        	updatedChunksQueue.remove(ac);
        	ac.detach();
        }
        chunkData.setToBePatched(false);
    }

    private void executePatchCommand(int command, int threshold, Chunk chunk, Season season)
    {
        // TODO: Handle client notification on block changes properly!

        ChunkPos chunkPos = chunk.getPos();
        World world = chunk.getWorld();

        if (command == 4 || command == 5)
        {
            threshold = THR_PROB_MAX;
        }

        MutableBlockPos pos = new MutableBlockPos();
        for (int iX = 0; iX < 16; iX++)
        {
            for (int iZ = 0; iZ < 16; iZ++)
            {
                int height = chunk.getHeightValue(iX, iZ);
                pos.setPos(chunkPos.getXStart() + iX, height, chunkPos.getZStart() + iZ);

                BlockPos below = pos.down();

                if ((command == 1 || command == 2 || command == 4))
                {
                    // Apply snow in dependence of last rain time and apply ice
                    // in dependence of last time the season changed to cold
                    // (where canSnowAtTempInSeason have returned false before).
                    if (world.rand.nextInt(THR_PROB_MAX) < threshold)
                    {
                        if (SeasonASMHelper.canBlockFreezeInSeason(world, below, false, Season.WINTER))
                        {
                            // NOTE: Is a simplified freeze behavior
                            world.setBlockState(below, Blocks.ICE.getDefaultState(), 2);
                        }
                        else if (command != 1 && SeasonASMHelper.canSnowAtInSeason(world, pos, true, Season.WINTER))
                        {
                            world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState(), 2);
                        }
                    }
                    // TODO: Simulate crop death
                }
                else if (command == 3 || command == 5)
                {
                    // Remove snow and ice in dependence of last time the season
                    // changed to cold (where canSnowAtTempInSeason have
                    // returned true before).
                    if (world.rand.nextInt(THR_PROB_MAX) <= threshold * 10)
                    {
                        IBlockState blockState = world.getBlockState(pos);
                        if (blockState.getBlock() == Blocks.SNOW_LAYER)
                        {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                        }
                        else
                        {
                            blockState = world.getBlockState(below);
                            if (blockState.getBlock() == Blocks.ICE)
                            {
                                world.setBlockState(below, Blocks.WATER.getDefaultState(), 2);
                                world.neighborChanged(below, Blocks.WATER, below);
                            }
                        }
                    }
                }
            }
        }
    }

    private void executePatchCommand(int command, long snowyTrackTicks, long rainingTrackTicks, Chunk chunk, Season season)
    {
        if (command != 0)
        {
            int threshold = 0;
            if (command == 2)
            {
                long dur = rainingTrackTicks;
                if (dur > snowyTrackTicks)
                    dur = snowyTrackTicks;
                threshold = evalProbUpdateTick((int) dur);
            }
            else if (command == 1 || command == 3)
                threshold = evalProbUpdateTick((int) snowyTrackTicks);
            executePatchCommand(command, threshold, chunk, season);
        }
    }

    private void patchChunkTerrain(ChunkData chunkData)
    {
        Chunk chunk = chunkData.getChunk();
        World world = chunk.getWorld();

        Season season = SeasonHelper.getSeasonData(world).getSubSeason().getSeason();
        SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);

        long lastPatchedTime = chunkData.getLastPatchedTime();
        boolean bFastForward = false;
        long windowBorder = world.getTotalWorldTime() - RETROSPECTIVE_WINDOW_TICKS;
        if (lastPatchedTime < windowBorder)
        {
            // Old entries have no effect. Considering it by reseting chunk snow
            // states and patch from newer journal entries only
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
        if (bFastForward)
        {
            if (bWasSnowy)
                executePatchCommand(4, 0, chunk, season);
            else
                executePatchCommand(5, 0, chunk, season);
        }

        // Replay latest journal entries
        if (fromIdx != -1)
        {
            int command = 0; // 0 = NOP. 1 = simulate freeze only. 2 = simulate
                             // chunk snow (requires ticks) 3 = simulate melting
                             // (requires ticks) 4 = set all snow 5 = set all
                             // molten

            // Apply events from journal
            for (int curEntry = fromIdx; curEntry < seasonData.journal.size(); curEntry++)
            {
                WeatherJournalEvent wevt = seasonData.journal.get(curEntry);

                rainingTrackTicks = wevt.getTimeStamp() - intervalRainingTrackStart;
                snowyTrackTicks = wevt.getTimeStamp() - intervalSnowyTrackStart;

                switch (wevt.getEventType())
                {
                    case EVENT_START_RAINING:
                        if (!bWasRaining)
                        {
                            intervalRainingTrackStart = wevt.getTimeStamp();
                            command = 0;
                            bWasRaining = true;
                        }
                        break;
                    case EVENT_STOP_RAINING:
                        if (bWasRaining)
                        {
                            intervalRainingTrackStart = wevt.getTimeStamp();
                            if (bWasSnowy)
                                command = 2;
                            else
                                command = 0;
                            bWasRaining = false;
                        }
                        break;
                    case EVENT_TO_COLD_SEASON:
                        if (!bWasSnowy)
                        {
                            intervalSnowyTrackStart = wevt.getTimeStamp();
                            command = 3;
                            bWasSnowy = true;
                        }
                        break;
                    case EVENT_TO_WARM_SEASON:
                        if (bWasSnowy)
                        {
                            intervalSnowyTrackStart = wevt.getTimeStamp();
                            if (bWasRaining)
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

                executePatchCommand(command, snowyTrackTicks, rainingTrackTicks, chunk, season);
            }
        }

        // Post update for running events
        rainingTrackTicks = world.getTotalWorldTime() - intervalRainingTrackStart;
        snowyTrackTicks = world.getTotalWorldTime() - intervalSnowyTrackStart;

        if (seasonData.wasLastRaining(-1) && seasonData.wasLastSnowy(-1))
        {
            executePatchCommand(2, snowyTrackTicks, rainingTrackTicks, chunk, season);
        }
        else if (!seasonData.wasLastSnowy(-1))
        {
            executePatchCommand(3, snowyTrackTicks, rainingTrackTicks, chunk, season);
        }
        else
        {
            executePatchCommand(1, snowyTrackTicks, rainingTrackTicks, chunk, season);
        }

        chunkData.setPatchTimeUptodate();
    }

    private int evalProbUpdateTick(int duringTicks)
    {
        final double fieldHitProb = 1.0 / (16.0 * 16.0);
        final double snowUpdateProbInTick = 1.0 / 16.0;
        final double correctionFactor = 0.75;
        final double hitProb = correctionFactor * fieldHitProb * snowUpdateProbInTick;
        final double missProb = 1.0 - hitProb;
        double prob = hitProb * (1.0 - Math.pow(missProb, duringTicks + 1)) / (1.0 - missProb);

        return (int) ((double) THR_PROB_MAX * prob + 0.5);
    }
    
    private static class PendingChunkEntry
    {
    	private final ChunkKey key;
    	private final World world;
    	private Chunk chunk;
    	
    	public PendingChunkEntry(Chunk chunk)
    	{
    		this.world = chunk.getWorld();
    		this.key = new ChunkKey(chunk.getPos(), this.world);
    		this.chunk = chunk;
    	}
    	
    	public PendingChunkEntry(ChunkKey key, World world)
    	{
    		this.key = key;
    		this.world = world;
    		this.chunk = null;
    	}
    	
    	public ChunkKey getKey() {
    		return this.key;
    	}
    	
    	public World getWorld() {
    		return this.world;
    	}
    	
    	public Chunk getChunk() {
    		if( chunk == null ) {
    			chunk = world.getChunkFromChunkCoords(key.getPos().chunkXPos, key.getPos().chunkZPos);
    		}
    		return chunk;
    	}
    }
}

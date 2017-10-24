package toughasnails.handler.season;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.season.SeasonChunkPatcher;

public class SeasonChunkPatchingHandler
{

    @SubscribeEvent
    public void chunkLoad(ChunkDataEvent.Load event)
    {
        if (event.getWorld().isRemote)
            return;
        SeasonChunkPatcher patcher = SeasonHandler.getSeasonChunkPatcher();

        Chunk chunk = event.getChunk();
        if (chunk.isTerrainPopulated())
        {
            patcher.enqueueChunkOnce(chunk);
        }
    }

    @SubscribeEvent
    public void postPopulate(PopulateChunkEvent.Post event)
    {
        World world = event.getWorld();
        if (world.isRemote)
            return;
        SeasonChunkPatcher patcher = SeasonHandler.getSeasonChunkPatcher();

        Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
        patcher.enqueueChunkOnce(chunk);
        patcher.enqueueGeneratedNeighborChunks(world, event.getChunkX(), event.getChunkZ());
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.side == Side.SERVER && (world instanceof WorldServer))
        {
            // NOTE: Should never happen, that world isn't an instance of
            // WorldServer, but just for sure ...
            SeasonChunkPatcher patcher = SeasonHandler.getSeasonChunkPatcher();
            patcher.onServerWorldTick((WorldServer) world);
        }
    }
}

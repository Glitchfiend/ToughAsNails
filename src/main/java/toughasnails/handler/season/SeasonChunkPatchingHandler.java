package toughasnails.handler.season;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.season.SeasonChunkPatcher;
import toughasnails.season.SeasonSavedData;

public class SeasonChunkPatchingHandler
{
    private static SeasonChunkPatcher chunkPatcher = new SeasonChunkPatcher();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDebugOverlay(final RenderGameOverlayEvent.Text event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc.gameSettings.showDebugInfo)
            {
                event.getLeft().add("" + chunkPatcher.statisticsVisitedActive + " active chunks were visited.");
                event.getLeft().add("" + chunkPatcher.statisticsAddedToActive + " active chunks were added.");
                event.getLeft().add("" + chunkPatcher.statisticsDeletedFromActive + " active chunks were deleted.");
                event.getLeft().add("" + chunkPatcher.statisticsPendingAmount + " chunks enqueued for patching.");
                event.getLeft().add("" + chunkPatcher.statisticsRejectedPendingAmount + " chunks got rejected from patching.");
            }
        }

    }

    @SubscribeEvent
    public void chunkDataLoad(ChunkEvent.Load event)
    {
        if (event.getWorld().isRemote)
            return;

        Chunk chunk = event.getChunk();
        if (chunk.isTerrainPopulated())
        {
            chunkPatcher.enqueueChunkOnce(chunk);
            chunkPatcher.notifyLoadedAndPopulated(chunk.getWorld(), chunk.getPos());
        }
    }

    @SubscribeEvent
    public void chunkUnload(ChunkEvent.Unload event)
    {
        if (event.getWorld().isRemote)
            return;

        Chunk chunk = event.getChunk();
        chunkPatcher.onChunkUnload(chunk);
    }

    @SubscribeEvent
    public void postPopulate(PopulateChunkEvent.Post event)
    {
        World world = event.getWorld();
        if (world.isRemote)
            return;

        ChunkPos pos = new ChunkPos(event.getChunkX(), event.getChunkZ());
        chunkPatcher.enqueueChunkOnce(world, pos);
        chunkPatcher.notifyLoadedAndPopulated(world, pos);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.side == Side.SERVER && (world instanceof WorldServer))
        {
            // NOTE: Should never happen, that world isn't an instance of
            // WorldServer, but just for sure ...
            chunkPatcher.onServerWorldTick((WorldServer) world);
        }
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event)
    {
        World world = event.getWorld();
        if (world.isRemote)
            return;

        // Clear loadedChunkQueue
        chunkPatcher.onServerWorldUnload(world);
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event)
    {
        // Performs pending patching tasks
        chunkPatcher.onServerTick();
    }

    public static SeasonChunkPatcher getSeasonChunkPatcher()
    {
        return chunkPatcher;
    }
}

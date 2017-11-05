/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import toughasnails.api.season.Season;
import toughasnails.core.ToughAsNails;
import toughasnails.util.DataUtils;
import toughasnails.util.IDataStorable;

public class SeasonSavedData extends WorldSavedData
{
    public static final String DATA_IDENTIFIER = "seasons";

    public int seasonCycleTicks;

    private boolean isLastSnowyState = false;
    private boolean isLastRainyState = false;
    public List<WeatherJournalEvent> journal = new ArrayList<WeatherJournalEvent>();

    public HashMap<ChunkKey, ChunkData> managedChunks = new HashMap<ChunkKey, ChunkData>();

    public SeasonSavedData()
    {
        this(DATA_IDENTIFIER);
    }

    // This specific constructor is required for saving to occur
    public SeasonSavedData(String identifier)
    {
        super(identifier);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.seasonCycleTicks = nbt.getInteger("SeasonCycleTicks");
        try
        {
            this.journal = DataUtils.toListStorable(nbt.getByteArray("WeatherJournal"), WeatherJournalEvent.class);
        }
        catch (IOException e)
        {
            ToughAsNails.logger.error("Couldn't retrieve weather journal. Use a clear one.", e);
            this.journal = new ArrayList<WeatherJournalEvent>();
        }

        try
        {
            List<ChunkDataStorage> timeStamps = DataUtils.toListStorable(nbt.getByteArray("ChunkExtraInfo"), ChunkDataStorage.class);
            applyLastPatchedTimes(timeStamps);
        }
        catch (IOException e)
        {
            ToughAsNails.logger.error("Couldn't load chunk patch timestamps. Some chunks won't be in synch with season.", e);
        }

        determineLastState();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("SeasonCycleTicks", this.seasonCycleTicks);
        try
        {
            nbt.setByteArray("WeatherJournal", DataUtils.toBytebufStorable(journal));
        }
        catch (IOException e)
        {
            ToughAsNails.logger.error("Couldn't store weather journal.", e);
        }

        try
        {
            nbt.setByteArray("ChunkExtraInfo", DataUtils.toBytebufStorable(toLastPatchedTimeStorable()));
        }
        catch (IOException e)
        {
            ToughAsNails.logger.error("Couldn't store chunk patch timestamps. Some chunks won't be in synch with season.", e);
        }

        return nbt;
    }

    private List<ChunkDataStorage> toLastPatchedTimeStorable()
    {
        int size = managedChunks.size();
        ArrayList<ChunkDataStorage> result = new ArrayList<ChunkDataStorage>(size);
        for (Map.Entry<ChunkKey, ChunkData> entry : managedChunks.entrySet())
        {
            result.add(new ChunkDataStorage(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private void applyLastPatchedTimes(List<ChunkDataStorage> list)
    {
        for (ChunkDataStorage entry : list)
        {
            ChunkData data = managedChunks.get(entry.getKey());
            if (data != null)
            {
                data.setPatchTimeTo(data.getLastPatchedTime());
            }
            else
            {
                data = new ChunkData(entry.getKey(), null, entry.getLastPatchedTime());
                managedChunks.put(entry.getKey(), data);
            }
        }
    }

    private void determineLastState()
    {
        int lastSnowyState = -1;
        int lastRainyState = -1;
        for (int i = journal.size() - 1; i >= 0; i--)
        {
            WeatherJournalEvent je = journal.get(i);
            WeatherEventType etype = je.getEventType();

            switch (etype)
            {
                case EVENT_TO_COLD_SEASON:
                    if (lastSnowyState == -1)
                        lastSnowyState = 1;
                    break;
                case EVENT_TO_WARM_SEASON:
                    if (lastSnowyState == -1)
                        lastSnowyState = 0;
                    break;
                case EVENT_START_RAINING:
                    if (lastRainyState == -1)
                        lastRainyState = 1;
                    break;
                case EVENT_STOP_RAINING:
                    if (lastRainyState == -1)
                        lastRainyState = 0;
                    break;
                case EVENT_UNKNOWN:
                    ToughAsNails.logger.warn("Unknown weather journal entry found.");
            }

            // Is now fully determined?
            if (lastSnowyState != -1 && lastRainyState != -1)
                break;
        }

        isLastSnowyState = (lastSnowyState == 1); // -1 state is Default: First
                                                  // minecraft day is at spring.
        isLastRainyState = (lastRainyState == 1); // -1 state is Default: First
                                                  // minecraft day has no rain.
    }

    public boolean wasLastRaining(int atIdx)
    {
        if (atIdx != -1)
        {
            for (int i = atIdx; i < journal.size(); i++)
            {
                WeatherJournalEvent je = journal.get(i);
                WeatherEventType etype = je.getEventType();

                switch (etype)
                {
                    case EVENT_START_RAINING:
                        return false;
                    case EVENT_STOP_RAINING:
                        return true;
                    default:
                }
            }
        }

        return isLastRainyState;
    }

    public boolean wasLastSnowy(int atIdx)
    {
        if (atIdx != -1)
        {
            for (int i = atIdx; i < journal.size(); i++)
            {
                WeatherJournalEvent je = journal.get(i);
                WeatherEventType etype = je.getEventType();

                switch (etype)
                {
                    case EVENT_TO_COLD_SEASON:
                        return false;
                    case EVENT_TO_WARM_SEASON:
                        return true;
                    default:
                }
            }
        }

        return isLastSnowyState;
    }

    public int getJournalIndexAfterTime(long timeStamp)
    {
        // TODO: Use subdivision to find the time point in approx. O(log n)
        // steps.

        for (int i = 0; i < journal.size(); i++)
        {
            if (journal.get(i).getTimeStamp() >= timeStamp)
                return i;
        }

        return -1;
    }

    private void addEvent(World w, WeatherEventType eventType)
    {
        switch (eventType)
        {
            case EVENT_TO_COLD_SEASON:
                isLastSnowyState = true;
                break;
            case EVENT_TO_WARM_SEASON:
                isLastSnowyState = false;
                break;
            case EVENT_START_RAINING:
                isLastRainyState = true;
                break;
            case EVENT_STOP_RAINING:
                isLastRainyState = false;
                break;
            case EVENT_UNKNOWN:
                ToughAsNails.logger.warn("Unknown weather event added. Ignoring");
                return;
        }

        journal.add(new WeatherJournalEvent(w.getTotalWorldTime(), eventType));
    }

    public void updateJournal(World w, Season curSeason)
    {
        if (curSeason == Season.WINTER && !wasLastSnowy(-1))
            addEvent(w, WeatherEventType.EVENT_TO_COLD_SEASON);
        else if (curSeason != Season.WINTER && wasLastSnowy(-1))
            addEvent(w, WeatherEventType.EVENT_TO_WARM_SEASON);

        if (w.isRaining() && !wasLastRaining(-1))
            addEvent(w, WeatherEventType.EVENT_START_RAINING);
        else if (!w.isRaining() && wasLastRaining(-1))
            addEvent(w, WeatherEventType.EVENT_STOP_RAINING);
    }

    public ChunkData getStoredChunkData(Chunk chunk, boolean bCreateIfNotExisting)
    {
        ChunkPos cpos = chunk.getPos();
        ChunkKey key = new ChunkKey(cpos, chunk.getWorld());
        ChunkData chunkData = managedChunks.get(key);
        if (chunkData != null)
        {
            Chunk curChunk = chunkData.getChunk();
            if (curChunk != null)
            {
                if (curChunk != chunk)
                {
                    if (!curChunk.unloaded)
                        ToughAsNails.logger.error("Chunk mismatching in SeasonSavedData.getStoredChunkData .");
                    curChunk = null;
                }
            }

            if (curChunk == null)
            {
                if (bCreateIfNotExisting)
                {
                    chunkData.setLoadedChunk(chunk);
                }
                else
                    return null;
            }
            return chunkData;
        }
        if (!bCreateIfNotExisting)
            return null;

        long lastPatchTime = 0; // Initial time. Should be bigger than
                                // ActiveChunkData.getSmallerKey() value!

        chunkData = new ChunkData(key, chunk, lastPatchTime);
        managedChunks.put(key, chunkData);
        return chunkData;
    }

    public ChunkData getStoredChunkData(ChunkKey key, boolean bCreateIfNotExisting)
    {
        ChunkData chunkData = managedChunks.get(key);
        if (chunkData == null && bCreateIfNotExisting)
        {
            long lastPatchTime = 0; // Initial time. Should be bigger than
                                    // ActiveChunkData.getSmallerKey() value!

            chunkData = new ChunkData(key, null, lastPatchTime);
            managedChunks.put(key, chunkData);
        }
        return chunkData;
    }

    public ChunkData getStoredChunkData(World world, ChunkPos pos, boolean bCreateIfNotExisting)
    {
        ChunkKey key = new ChunkKey(pos, world);
        return getStoredChunkData(key, bCreateIfNotExisting);
    }

    public void onWorldUnload(World world)
    {
        // Clear managed chunk tags associated to the world
        managedChunks.clear(); // No BUG: managedChunks contains only chunks
                               // associated to the world.
    }

    public void notifyChunkUnloaded(Chunk chunk)
    {
        ChunkKey key = new ChunkKey(chunk.getPos(), chunk.getWorld());
        ChunkData chunkData = managedChunks.get(key);
        if (chunkData != null)
        {
            chunkData.clearLoadedChunk();
        }
    }

    public static class ChunkDataStorage implements IDataStorable
    {
        private ChunkKey key;
        private long lastPatchedTime;

        public ChunkDataStorage()
        {
            // For streaming
        }

        public ChunkDataStorage(ChunkKey key, ChunkData data)
        {
            this.key = key;
            this.lastPatchedTime = data.getLastPatchedTime();
        }

        public ChunkKey getKey()
        {
            return key;
        }

        public long getLastPatchedTime()
        {
            return lastPatchedTime;
        }

        @Override
        public void writeToStream(ObjectOutputStream os) throws IOException
        {
            os.writeInt(key.getPos().chunkXPos);
            os.writeInt(key.getPos().chunkZPos);
            os.writeInt(key.getDimension());
            os.writeLong(lastPatchedTime);
        }

        @Override
        public void readFromStream(ObjectInputStream is) throws IOException
        {
            int chunkXPos = is.readInt();
            int chunkZPos = is.readInt();
            int dimension = is.readInt();
            this.key = new ChunkKey(new ChunkPos(chunkXPos, chunkZPos), dimension);
            this.lastPatchedTime = is.readLong();
        }
    }
}

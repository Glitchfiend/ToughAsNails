package toughasnails.season;

import net.minecraft.world.World;

public class ActiveChunkData
{
    private final ChunkData data;
    private final World world;
    private boolean isVisited;
    private long lastVisitTime;

    ActiveChunkData(ChunkData data, World world)
    {
        this.data = data;
        this.world = world;
        this.isVisited = false;
        this.lastVisitTime = 0;

        data.setBelongingAC(this);
    }

    ChunkKey getKey()
    {
        return data.getKey();
    }

    ChunkData getData()
    {
        return data;
    }

    World getWorld()
    {
        return world;
    }

    long getLastVisitTime()
    {
        return lastVisitTime;
    }

    public boolean isVisited()
    {
        return isVisited;
    }

    public void setVisited()
    {
        this.isVisited = true;
        this.lastVisitTime = world.getTotalWorldTime();
    }

    public void clearVisited()
    {
        this.isVisited = false;
    }
    
    public void detach() {
    	data.setBelongingAC(null);
    }
}
package toughasnails.season;

import net.minecraft.world.World;
import toughasnails.util.BinaryHeapNode;

public class ActiveChunkData extends BinaryHeapNode<Long>
{
    private final ChunkData data;
    private final World world;
    private long lastVisitTime;

    ActiveChunkData(ChunkData data, World world)
    {
        this.data = data;
        this.world = world;
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

    public void detach() {
    	data.setBelongingAC(null);
    }

	@Override
	public int compareTo(BinaryHeapNode<Long> o) {
		return Long.compare(this.lastVisitTime, o.getNodeKey());
	}

	@Override
	public Long getSmallerKey() {
		return -1L;
	}

	@Override
	public void setNodeKey(Long key) {
		this.lastVisitTime = key;
	}

	@Override
	public long getNodeKey() {
		return this.lastVisitTime;
	}
}
package toughasnails.season;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class ChunkKey {
	private ChunkPos pos;
    private int dimension;
    private String levelName;
	
	public ChunkKey(ChunkPos pos, World world) {
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
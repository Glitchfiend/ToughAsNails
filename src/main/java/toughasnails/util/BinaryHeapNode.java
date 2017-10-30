package toughasnails.util;

public abstract class BinaryHeapNode<K> implements Comparable<BinaryHeapNode<K>> {
	int index;
	
	protected BinaryHeapNode() {
		this.index = 0;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isEnqueued() {
		return index >= 1;
	}
	
	public abstract K getSmallerKey();
	public abstract void setNodeKey(K key);
	public abstract long getNodeKey();

/*	@Override
	public int compareTo(BinaryHeapNode o) {
		long t = key - o.key;
		if( t < 0 )
			return -1;
		else if( t > 0 )
			return 1;
		else
			return 0;
	} */
}

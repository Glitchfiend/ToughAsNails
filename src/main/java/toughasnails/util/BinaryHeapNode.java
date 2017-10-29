package toughasnails.util;

public abstract class BinaryHeapNode<K> implements Comparable<BinaryHeapNode<K>> {
	int index;
	
	public int getIndex() {
		return index;
	}
	
	public abstract K getSmallerKey();
	public abstract void setKey(K key);

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

package toughasnails.util;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Originally from https://courses.cs.washington.edu/courses/cse373/11wi/homework/5/BinaryHeap.java
 * 
 * @param <T>
 */
public class BinaryHeap<K, T extends BinaryHeapNode<K>> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    protected T[] array;
    protected int size;
    
    /**
     * Constructs a new BinaryHeap.
     */
    @SuppressWarnings("unchecked")
	public BinaryHeap () {
        // Java doesn't allow construction of arrays of placeholder data types 
        array = (T[])new BinaryHeapNode[DEFAULT_CAPACITY];  
        size = 0;
    }
    
    /**
     * Adds a value to the min-heap.
     */
    public void add(T value) {
        // grow array if needed
        if (size >= array.length - 1) {
            array = this.resize();
        }        
        
        // place element into heap at bottom
        size++;
        int index = size;
        array[index] = value;
        value.index = index;
        
        bubbleUp(this.size);
    }
    
    
    /**
     * Returns true if the heap has no elements; false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    
    /**
     * Returns (but does not remove) the minimum element in the heap.
     */
    public T peek() {
        if (this.isEmpty()) {
            return null;
        }
        
        return array[1];
    }
    
    /**
     * Remove at index
     */
    public void remove( T elem ) {
    	int index = elem.index;
    	if( !elem.isEnqueued() )
    		return;	// else, index >= 1 assumed.    	
    	if( size < index || array[index] != elem )
    		throw new IllegalStateException();
    	if( index <= 1 )
    	{
    		remove();
    		return;
    	}
    	
     	BinaryHeapNode<K> first = peek();
    	elem.setNodeKey( first.getSmallerKey() );
    	bubbleUp(index);
    	remove();
    }
    
    /**
     * Removes and returns the minimum element in the heap.
     */
    public T remove() {
    	// what do want return?
    	T result = peek();
    	if( result == null )
    		return null;
    	result.index = 0;
    	
    	// get rid of the last leaf/decrement
    	array[1] = array[size];
    	array[1].index = 1;
    	array[size] = null;
    	size--;
    	
    	bubbleDown(1);
    	return result;
    }
    
    /**
     * Returns a String representation of BinaryHeap with values stored with 
     * heap structure and order properties.
     */
    public String toString() {
        return Arrays.toString(array);
    }

    
    /**
     * Performs the "bubble down" operation to place the element that is at the 
     * root of the heap in its correct place so that the heap maintains the 
     * min-heap order property.
     */
    protected void bubbleDown(int index) {
       
        // bubble down
        while (hasLeftChild(index)) {
            // which of my children is smaller?
            int smallerChild = leftIndex(index);
            
            // bubble with the smaller child, if I have a smaller child
            if (hasRightChild(index)
                && array[leftIndex(index)].compareTo(array[rightIndex(index)]) > 0) {
                smallerChild = rightIndex(index);
            } 
            
            if (array[index].compareTo(array[smallerChild]) > 0) {
                swap(index, smallerChild);
            } else {
                // otherwise, get outta here!
                break;
            }
            
            // make sure to update loop counter/index of where last el is put
            index = smallerChild;
        }        
    }
    
    
    /**
     * Performs the "bubble up" operation to place a newly inserted element 
     * (i.e. the element that is at the size index) in its correct place so 
     * that the heap maintains the min-heap order property.
     */
    protected void bubbleUp( int index ) {
        while (hasParent(index)
                && (parent(index).compareTo(array[index]) > 0)) {
            // parent/child are out of order; swap them
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }        
    }
    
    
    protected boolean hasParent(int i) {
        return i > 1;
    }
    
    
    protected int leftIndex(int i) {
        return i * 2;
    }
    
    
    protected int rightIndex(int i) {
        return i * 2 + 1;
    }
    
    
    protected boolean hasLeftChild(int i) {
        return leftIndex(i) <= size;
    }
    
    
    protected boolean hasRightChild(int i) {
        return rightIndex(i) <= size;
    }
    
    
    protected BinaryHeapNode<K> parent(int i) {
        return array[parentIndex(i)];
    }
    
    
    protected int parentIndex(int i) {
        return i / 2;
    }
    
    
    protected T[] resize() {
        return Arrays.copyOf(array, array.length * 2);
    }
    
	public void clear() {
		for( int i = 1; i <= size; i ++ ) {
			array[i] = null;
		}
		size = 0;
	}
    
    protected void swap(int index1, int index2) {
    	T tmp = array[index1];
        
    	array[index1] = array[index2];
        if( array[index1] != null )
        	array[index1].index = index1;
        array[index2] = tmp;
        if( array[index2] != null )
        	array[index2].index = index2;
    }

	@Override
	public Iterator<T> iterator() {
		return new BinaryHeapIterator();
	}
	
	private class BinaryHeapIterator implements Iterator<T> {
		int curIndex = 0;

		@Override
		public boolean hasNext() {
			return curIndex < size;
		}

		@Override
		public T next() {
			return array[++ curIndex];
		}
	}


}
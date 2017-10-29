package toughasnails.util;

import java.util.Arrays;

/**
 * Originally from https://courses.cs.washington.edu/courses/cse373/11wi/homework/5/BinaryHeap.java
 * 
 * @param <T>
 */
public class BinaryHeap<K> {
    private static final int DEFAULT_CAPACITY = 10;
    protected BinaryHeapNode<K>[] array;
    protected int size;
    
    /**
     * Constructs a new BinaryHeap.
     */
    @SuppressWarnings("unchecked")
	public BinaryHeap () {
        // Java doesn't allow construction of arrays of placeholder data types 
        array = (BinaryHeapNode[])new Comparable[DEFAULT_CAPACITY];  
        size = 0;
    }
    
    /**
     * Adds a value to the min-heap.
     */
    public void add(BinaryHeapNode<K> value, long withKey) {
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
    public BinaryHeapNode<K> peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException();
        }
        
        return array[1];
    }
    
    /**
     * Remove at index
     */
    public BinaryHeapNode<K> remove( BinaryHeapNode<K> elem ) {
    	int index = elem.index;
    	if( array.length <= index || array[index] != elem )
    		throw new IllegalStateException();
    	if( index == 1 )
    		return remove();
    	
    	BinaryHeapNode<K> first = peek();
    	elem.setKey( first.getSmallerKey() );
    	bubbleUp(index);
    	
    	return remove();
    }
    
    /**
     * Removes and returns the minimum element in the heap.
     */
    public BinaryHeapNode<K> remove() {
    	// what do want return?
    	BinaryHeapNode<K> result = peek();
    	
    	// get rid of the last leaf/decrement
    	array[1] = array[size];
    	array[size] = null;
    	size--;
    	
    	bubbleDown();
    	
    	result.index = 0;
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
    protected void bubbleDown() {
        int index = 1;
        
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
    
    
    protected BinaryHeapNode<K>[] resize() {
        return Arrays.copyOf(array, array.length * 2);
    }
    
    protected void swap(int index1, int index2) {
    	BinaryHeapNode<K> tmp = array[index1];
        
    	array[index1] = array[index2];
        if( array[index1] != null )
        	array[index1].index = index1;
        array[index2] = tmp;
        if( array[index2] != null )
        	array[index2].index = index2;
    }
}
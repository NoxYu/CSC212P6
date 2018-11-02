package edu.smith.cs.csc212.p6;

import edu.smith.cs.csc212.p6.errors.*;

/** 
 * This is a data structure that has an array inside each node of a Linked List.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyLinkedList<T> implements P6List<T> {
	private int chunkSize;
	private SinglyLinkedList<FixedSizeList<T>> chunks;
		 
	/*
	 * efficiency: O(1)
	 */
	public ChunkyLinkedList(int chunkSize) {
		this.chunkSize = chunkSize;
		//chunks.addBack(new FixedSizeList<>(chunkSize));
		/*
		 * this would cause a null pointer exception
		 * because chunks has not been initialized
		 */
		chunks = new SinglyLinkedList<FixedSizeList<T>>();
		chunks.addBack(new FixedSizeList<>(chunkSize));
	}

	/*
	 * efficiency: O(n^2)
	 * method getIndex (uses a for loop) is inside the for loop 
	 */
	@Override
	public T removeFront() {
		if(isEmpty()) {
			throw new EmptyListError();
		}
		FixedSizeList<T> firstChunk = chunks.getFront();
		T removed = firstChunk.getFront();
		//easiest: if there is only one chunk and it is not full
		if(chunks.size()==1) {
			firstChunk.removeFront();
		}else {
			for(int i=0;i<chunks.size()-1;i++) {
				FixedSizeList<T> current=chunks.getIndex(i);
				FixedSizeList<T> post=chunks.getIndex(i+1);
				current.removeFront();
				current.addBack(post.getFront());
				if(i+1==chunks.size()-1) {
					post.removeFront();
				}
				if(chunks.getBack().size()==0) {
					chunks.removeBack();
				}
			}
		}
		return removed;
	}

	//efficiency: O(n) 
	@Override
	public T removeBack() {
		if(isEmpty()) {
			throw new EmptyListError();
		}
		//tricky:when there is only one element left in the last chunk
		FixedSizeList<T> lastChunk = chunks.getBack();
		T removed = lastChunk.getBack();
		if(lastChunk.size()==1) {
			lastChunk.removeBack();
			chunks.removeBack();
		}else {
			lastChunk.removeBack();
		}
		return removed;
	}

	//O(n^2)
	@Override
	public T removeIndex(int index) {
		if(chunks.getFront().size()==0) {
			throw new EmptyListError();
		}
		
		int maxIndex = chunkSize*chunks.size()-1;
		if(index<0||index>maxIndex) {
			throw new BadIndexError();
		}		
		
		/*
		 * first, find and remove the element
		 * to find it, we need to know which chunk and where in that chunk it is
		 */
		int chunkIndex=(int)( ((index)/chunkSize) );
		int listIndex=(int)( ((index)%chunkSize) );
				
		//store the last element for the return statement
		T removed = chunks.getIndex(chunkIndex).removeIndex(listIndex);
	
		/*
		 * now rearrange the list
		 * should be similar to what we did in removeFront 
		 */
		for(int i=chunkIndex;i<chunks.size()-1;i++) {
			FixedSizeList<T> current = chunks.getIndex(i);
			FixedSizeList<T> post = chunks.getIndex(i+1);
			current.addBack(post.removeFront());
		}
		if(chunks.getBack().size()==0) {
			chunks.removeBack();
		}
		return removed;
	}

	//O(n^2)
	@Override
	public void addFront(T item) {
		if(isEmpty()) {
			chunks.addFront(new FixedSizeList<T>(chunkSize));
			chunks.getFront().addFront(item);
			return;
		}
		FixedSizeList<T> firstChunk = chunks.getFront();
		if(firstChunk.size()<chunkSize) {
			//easy if the first chunk is not full
			firstChunk.addFront(item);
			return;
		}else {
			if(chunks.size()==1){
				/*
				 * if there is only one chunk and it is full, 
				 * we need to add a new chunk to store the last element of the first chunk
				 */
				T temp = firstChunk.removeBack();
				firstChunk.addFront(item);
				chunks.addBack(new FixedSizeList<T>(chunkSize));
				chunks.getBack().addFront(temp);
			}else {
				 /*
				  * we start from the end of the list, moving forward
				  * each time we remove the last element of the current chunk
				  * and add the last element of the previous chunk to the front of the current chunk
				  * No need to worry about losing the last element
				  * it is already stored in last
				  * which we will add once the for loop is over
				  */
				T last = chunks.getBack().getBack();
				for(int i=chunks.size()-1;i>0;i--) {
					FixedSizeList<T> current = chunks.getIndex(i);
					FixedSizeList<T> prev= chunks.getIndex(i-1);
					current.removeBack();
					current.addFront(prev.getBack());
				}
				//we need to add a new chunk to store last if the last chunk is full
				if(chunks.getBack().size()==chunkSize-1) {
					chunks.addBack(new FixedSizeList<T>(chunkSize));
				}
				chunks.getBack().addBack(last);
				firstChunk.removeBack();
				firstChunk.addFront(item);				
			}
		}
	}

	//O(n)
	@Override
	public void addBack(T item) {
		FixedSizeList<T> lastChunk = chunks.getBack();
		if(lastChunk.size()<chunkSize) {
			lastChunk.addBack(item);
		}else {
			//if the chunk is full, we need to add a new chunk and put the item there			
			chunks.addBack(new FixedSizeList<T>(chunkSize));
			lastChunk=chunks.getBack();
			lastChunk.addBack(item);
		}
	}
	
	
	//O(n^2)
	@Override
	public void addIndex(T item, int index) {
		if(index==0) {
			addFront(item);
			return;
		}
		if(index==(chunkSize*chunks.size()-1)) {
			addBack(item);
			return;
		}
		if(index<0||index>=(chunkSize*chunks.size())) {
			throw new BadIndexError();
		}
		//find out the index of the chunk where we insert the new item
		int chunkIndex=(int)(((index)/chunkSize));
		int listIndex=(int)(((index)%chunkSize));
	
		/*
		 * several things to pay attention to:
		 * a) we should care whether the last chunk is already full or not
		 * b) we need to create space for the insertion
		 * c) to do that we need to store the last element of the last chunk
		 */
		FixedSizeList<T> lastChunk = chunks.getBack();
		/*
		 * let's create space first by moving all elements after the index backward
		 * this is similar to what we did in addFront
		 */
		T last = lastChunk.getBack();
		for(int i=chunks.size()-1;i>chunkIndex;i--) {
			FixedSizeList<T> current = chunks.getIndex(i);
			FixedSizeList<T> prev = chunks.getIndex(i-1);
			if(i-1==chunkIndex) {
				current.removeBack();
			}else {
				current.removeBack();
				current.addFront(prev.getBack());
			}
		}
		//add the item to the correct index to that chunk
		chunks.getIndex(chunkIndex).addIndex(item, listIndex);
		/*
		 * if the last chunk is full, add a new chunk to put last
		 */
		if(lastChunk.size()==chunkSize-1) {
			chunks.addBack(new FixedSizeList<T>(chunkSize));
		}
		chunks.getBack().addBack(last);
	}
	
	//O(1)
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}

	//O(1)
	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
	}

	//O(n^2)
	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError();
	}

	//O(n^2)
	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	//O(n)
	@Override
	public boolean isEmpty() {
		return this.chunks.size()==0;
	}
}

package edu.smith.cs.csc212.p6;

import edu.smith.cs.csc212.p6.errors.*;

public class GrowableList<T> implements P6List<T> {
	public static final int START_SIZE = 32;
	private Object[] array;
	private int fill; 
	
	//O(1)
	public GrowableList() {
		this.array = new Object[START_SIZE];
		this.fill = 0;
	}

	//O(n)
	@Override
	public T removeFront() {
		return removeIndex(0);
	}

	//O(1)
	@Override
	public T removeBack() {
		if(this.size()==0) {
			throw new EmptyListError();
		}
		T removed = this.getIndex(fill-1);
		fill--;
		this.array[fill]= null;
		return removed;
	}

	//O(n)
	@Override
	public T removeIndex(int index) {
		if(this.size()==0) {
			throw new EmptyListError();
		}
		fill--;
		T removed = this.getIndex(index);
		for(int i = index;i<fill;i++) {
			this.array[i]=this.array[i+1];
		}
		this.array[fill] = null;
		return removed;
	}

	//O(n)
	@Override
	public void addFront(T item) {
		if(fill<array.length) {
			for(int i=fill;i>0;i--) {
				this.array[i]=this.array[i-1];
			}
			this.array[0]=item;
			fill++;
		}else {
			int newSize = fill*2;
			Object[] newArray = new Object[newSize];
			for(int i=0;i<array.length;i++) {
				newArray[i+1] = array[i];
			}
			newArray[0] = item;
			fill++;
		}		
	}

	//O(n)
	@Override
	public void addBack(T item) {
		// I've implemented part of this for you.
		if (fill >= this.array.length) { 
			int newSize=fill*2;
			Object[] newArray = new Object[newSize];
			for(int i=0;i<array.length;i++) {
				newArray[i] = array[i];
			}
			newArray[fill++]=item;
			this.array=newArray;
		}
		this.array[fill++] = item;
	}
	
	//O(n)
	@Override
	public void addIndex(T item, int index) {
		if(index<0||index>=size()) {
			throw new BadIndexError();
		}
		if (fill >= this.array.length) { 
			int newSize=fill*2;
			Object[] newArray = new Object[newSize];
			for(int i=0;i<=array.length;i++) {
				if(i<index) {
					newArray[i]=array[i];
				}else if(i==index) {
					newArray[i]=item;
				}else {
					newArray[i]=array[i-1];
				}
			}
			this.array=newArray;
		}
	}
	
	//O(1)
	@Override
	public T getFront() {
		return this.getIndex(0);
	}

	//O(1)
	@Override
	public T getBack() {
		return this.getIndex(this.fill-1);
	}

	/**
	 * Do not allow unchecked warnings in any other method.
	 * Keep the "guessing" the objects are actually a T here.
	 * Do that by calling this method instead of using the array directly.
	 */
	@SuppressWarnings("unchecked")
	@Override
	//O(1)
	public T getIndex(int index) {
		return (T) this.array[index];
	}

	//O(1)
	@Override
	public int size() {
		return fill;
	}

	//O(1)
	@Override
	public boolean isEmpty() {
		return fill == 0;
	}


}

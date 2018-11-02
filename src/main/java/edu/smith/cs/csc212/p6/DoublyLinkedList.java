package edu.smith.cs.csc212.p6;

import edu.smith.cs.csc212.p6.errors.*;
 
public class DoublyLinkedList<T> implements P6List<T> {
	private Node<T> start;
	private Node<T> end;
	private int size;
	/**
	 * A doubly-linked list starts empty.
	 */
	//O(1)
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
		this.size =0;
	}
	
	//O(1)
	@Override
	public T removeFront() {
		checkNotEmpty();
		Node<T> deleted = start;
		start = start.after;
		return deleted.value;
	}
	
	//O(n)
	@Override
	public T removeBack() {
		checkNotEmpty();
		if(size()==1) {
			Node<T> deleted = start;
			start = null;
			return deleted.value;
		}else {
			Node<T> last = start;
			while(last.after!=null) {
				last=last.after;
			}
			last.before.after=null;		
			return last.value;
		}
	}

	//O(n)
	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		if(index>=size()||index<0) {
			throw new BadIndexError();
		}
		
		Node<T> deleted = start;
		T target=deleted.value;
		
		if(index==0) {
			if(size()==1) {
				target = start.value;
				start=null;
				return target;
			}
			target = start.value;
			start.after.before=null;
			return target;
		}
		if(index==size()-1) {
			while(deleted.after!=null) {
				deleted = deleted.after;
			}
			deleted.before.after = null;
			target = deleted.value;
			return target;
		}
		
		int count = 0;
		for(Node<T> current = start;current.after!=null;current = current.after) {
			if(count==index) {
				deleted = current;
				target = current.value;
				break;
			}
			count++;
		}
		if(deleted.before!=null) {
			deleted.before.after = deleted.after;
		}
		if(deleted.after!=null) {
			deleted.after.before=deleted.before;
		}
		
		return target;
	}

	//O(1)
	@Override
	public void addFront(T item) {
		if(size()==0) {
			start = new Node<T>(item);
		}else {
			Node<T> newStart = new Node<T>(item);
			newStart.after = start;
			start.before = newStart;
			start = newStart;
		}
	}

	//O(n)
	@Override
	public void addBack(T item) {
		if(isEmpty()) {
			start = new Node<T>(item);
		}else {
			Node<T> newEnd = new Node<T>(item);
			//we still have to find the last node of the list, tell the program that it is the end
			Node<T> last = start;
			while(last.after!=null) {
				last = last.after;
			}
			last.after = newEnd;
			newEnd.before = last;
			end = newEnd;
		}
	}

	//O(n)
	@Override
	public void addIndex(T item, int index) {
		if(index==0) {
			addFront(item);
			return;
		}
		if(index==size()) {
			addBack(item);
			return;
		}
		Node<T> post=start;
		for(int i=0;i<index;i++) {
			post=post.after;
		}
		Node<T> ante=post.before;
		/*
		 * the new node is in between ante and post
		 * the index of the inserted Node will be the same was ante's index now
		 * so:
		 * index of post is index
		 * index of ante is index-1
		 */
		Node<T> inserted=new Node<T>(item);
		inserted.before=ante;
		inserted.after=post;
		ante.after=inserted;
		post.before=inserted;
		
	}

	//O(1)
	@Override
	public T getFront() {
		return start.value;
	}

	//O(n)
	@Override
	public T getBack() {
		Node<T> current = start;
		while(current.after!=null) {
			current=current.after;
		}
		return current.value;
	}
	
	//O(n)
	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		if(index<0||index>size()-1) {
			throw new BadIndexError();	
		}
		int count=0;
		Node<T> target = start;
		for(Node<T> current=start; current!=null ; current = current.after){
			if(count==index) {
				target = current;
				break;
			}
			count++;
		}
		return target.value;
	}

	//O(n)
	@Override
	public int size() {
		int count = 0;
		for(Node<T> current = start; current!=null; current=current.after) {
			count++;
		}
		return count;
	}


	//O(n)
	@Override
	public boolean isEmpty() {
		if(size()==0) {
			return true;
		}else {
			return false;
		}
	}
	
	//O(n)
	private void checkNotEmpty() {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
	}
	
	
	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of DoublyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		//O(1)
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
}

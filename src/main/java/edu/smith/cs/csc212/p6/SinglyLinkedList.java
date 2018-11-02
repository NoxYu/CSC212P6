package edu.smith.cs.csc212.p6;

import java.util.Iterator;

import edu.smith.cs.csc212.p6.errors.*;

public class SinglyLinkedList<T> implements P6List<T>, Iterable<T> {
	/**
	 * The start of this list. Node is defined at the bottom of this file.
	 */
	Node<T> start;

	@Override
	public T removeFront() {
		checkNotEmpty();
		T targetValue = start.value;
		start = start.next;
		return targetValue;
	}

	@Override
	public T removeBack() {
		if(size()==0) {
			throw new EmptyListError();
		}else if(size()==1) {
			T targetValue = start.value;
			start=null;
			return targetValue;
		}else {
			Node<T> target = start;
			Node<T> last = start;
			for(Node<T> current=start;current.next!=null;current=current.next) {
				last=current.next;
				target=current;
			}
			target.next=null;
			return last.value;
		}
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		Node<T> target = start;
		Node<T> targetPrev = start;
		Node<T> current = start;
		int count=0;

		if(index<0||index>=size()) {
			throw new BadIndexError();
		}
		
		if(index==0) {
			removeFront();
		}else {
			for(int i=0;i<size();i++) {
				if(count==index-1) {
					targetPrev=current;
				}
				if(count==index) {
					target=current;
					break;
				}
				current = current.next;
				count++;
			}
		}
		targetPrev.next=target.next;
		return target.value;
	}

	@Override
	public void addFront(T item) {
		this.start = new Node<T>(item, start);
	}

	@Override
	public void addBack(T item) {
		if(size()==0) {
			start = new Node<T>(item, null);
			return;
		}
		Node<T> last = start;
		while(last.next!=null) {
			last = last.next;
		}
		Node<T> added = new Node<T>(item, null);
		last.next = added;
	}

	@Override
	public void addIndex(T item, int index) {
		if(index==0) {
			Node<T> oldStart=start;
			start=new Node<T>(item, oldStart);
			return;
		}
		//the target is the node after which we insert the new item
		Node<T> target = start;
		//so the new item is inserted between target and postTarget
		Node<T> postTarget = start;
		Node<T> current = start;
		int count = 0;
		for(int i=0;i<index;i++) {
			target=current;
			postTarget=current.next;
			current=current.next;
			count++;
		}
		Node<T> added=new Node<T>(item,postTarget);
		target.next=added;
	}

	@Override
	public T getFront() {
		return start.value;
	}

	@Override
	public T getBack() {
		return getIndex(size()-1);
	}

	@Override
	public T getIndex(int index) {
		if(index>size()||index<0) {
			throw new BadIndexError();
		}
		int count =0;
		T target = null;
		for(Node<T> current =start; current!=null; current = current.next) {
			if(count==index) {
				target = current.value;
				break;
			}
			count++;
		}
		return target;
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		if(size()==0) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * Helper method to throw the right error for an empty state.
	 */
	private void checkNotEmpty() {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of SinglyLinkedList to make a node.
	 * 
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with no friends.
		 * 
		 * @param value - the value to put in it.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

	/**
	 * I'm providing this class so that SinglyLinkedList can be used in a for loop
	 * for {@linkplain ChunkyLinkedList}. This Iterator type is what java uses for
	 * {@code for (T x : list) { }} lops.
	 * 
	 * @author jfoley
	 *
	 * @param <T>
	 */
	private static class Iter<T> implements Iterator<T> {
		/**
		 * This is the value that walks through the list.
		 */
		Node<T> current;

		/**
		 * This constructor details where to start, given a list.
		 * @param list - the SinglyLinkedList to iterate or loop over.
		 */
		public Iter(SinglyLinkedList<T> list) {
			this.current = list.start;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			T found = current.value;
			current = current.next;
			return found;
		}
	}
	
	/**
	 * Implement iterator() so that {@code SinglyLinkedList} can be used in a for loop.
	 * @return an object that understands "next()" and "hasNext()".
	 */
	public Iterator<T> iterator() {
		return new Iter<>(this);
	}
}

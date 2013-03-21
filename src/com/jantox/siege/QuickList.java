package com.jantox.siege;

import java.util.Iterator;

public class QuickList<E> implements Iterable<E>, Iterator<E> {
	
	private E[] objects;
	
	public QuickList() {
		
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}

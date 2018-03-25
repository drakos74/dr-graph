package dr.common.struct.stack;

import java.util.LinkedList;
import java.util.List;

public class LinkedListStack<T> implements Stack<T> {

	private final List<T> stack = new LinkedList<T>();
	
	@Override
	public boolean add(T t) {
		return stack.add(t);
	}
	
	@Override
	public T get() {
		if(isEmpty()) {
			throw new IllegalStateException("Cannot call remove on empty stack");
		}
		return stack.get(stack.size() - 1);
	}

	@Override
	public T remove() {
		if(isEmpty()) {
			throw new IllegalStateException("Cannot call remove on empty stack");
		}
		return stack.remove(stack.size() - 1);
	}

	@Override
	public long size() {
		return stack.size();
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public void clear() {
		stack.clear();
	}

	
	
}

package dr.common.struct.stack;

public interface Stack<T> {

	boolean add(T t);
	
	T get();
	
	T remove();
	
	long size();
	
	boolean isEmpty();
	
	void clear();
	
}

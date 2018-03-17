package dr.common.struct;

public interface Stack<T> {

	boolean add(T t);
	
	T get();
	
	T remove();
	
	long size();
	
	boolean isEmpty();
	
	void clear();
	
}

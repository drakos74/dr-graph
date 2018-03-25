package dr.graph.vm.parser;

import java.util.Optional;

public interface Pattern<C> {

	/**
	 * test the pattern
	 * */
	Optional<Boolean> test(Character ch); 
	
	/**
	 * make the object re-usable
	 */
	void reset();

	/**
	 * check if the pattern is complete / found
	 */
	boolean isComplete();

	/**
	 * check if pattern is currently in
	 */
	boolean isActive();
	
	enum Type {
		START, END , BLOCK;
	}
	
}

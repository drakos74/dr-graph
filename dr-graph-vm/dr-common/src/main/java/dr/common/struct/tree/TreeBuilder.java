package dr.common.struct.tree;

import dr.common.Builder;
import dr.common.logger.LazyLogger;

public abstract class TreeBuilder<T> implements Builder<Tree<T>> , LazyLogger{

	// TODO : add configuration item to specify what we should resolve in the build method
	protected final Node<T> head;
	
	@SuppressWarnings("unchecked")
	public TreeBuilder(Node<T> node , int lookUp , int lookDown) {
		// first we look up to find head ... 
		Node<T> pointer = node;
		int parentCounter = lookUp;
		log("starting loo-UP phase "+parentCounter+" at "+pointer);
		while(pointer != null && parentCounter > 0) {
			pointer = (Node<T>) pointer.parent();
			parentCounter--;
			log("reach loo-UP phase "+parentCounter+" at "+pointer);
		}
		head = pointer;
	}
	
	
	
}

package dr.common.struct.tree;

import java.util.ArrayList;
import java.util.List;

import dr.common.Builder;
import dr.common.logger.LazyLogger;

public abstract class TreeBuilder<T> implements Builder<Tree<T>> , LazyLogger{

	// TODO : add configuration item to specify what we should resolve in the build method
	protected final Node<T> head;
	protected int initLevel = 0;
	protected final int endLevel;
	
	@SuppressWarnings("unchecked")
	public TreeBuilder(Node<T> node , int lookUp , int lookDown) {
		endLevel = lookDown;
		// first we look up to find the head ... 
		Node<T> pointer = node;
		Node<T> tmpHead = pointer;
		int parentCounter = lookUp;
		log("starting look-UP phase "+parentCounter+" at "+pointer);
		while(pointer != null && parentCounter > 0) {
			pointer = (Node<T>) pointer.parent();
			parentCounter--;
			log("reach look-UP phase "+parentCounter+" at "+pointer);
			if(pointer != null) {
				tmpHead = pointer;
			}
		}
		initLevel = initLevel - parentCounter;
		head = tmpHead;
	}
	
	protected abstract Tree<T> constructTree();
	
	@Override
	public Tree<T> build() {
		
		List<Node<T>> thisLevelNodes = new ArrayList<>();
		thisLevelNodes.add(head);
		
		for(int i = initLevel ; i < endLevel ; i++) {
			
			thisLevelNodes
				.stream()
				.flatMap( parent -> parent
							.children()
							.values()
							.stream() /* + apply the properties for the structure and indexes */ );
			
		}
		
		return constructTree();
		
	}
	
}

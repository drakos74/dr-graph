package dr.common.struct.tree;

import dr.common.Builder;

public abstract class TreeBuilder<T> implements Builder<Tree<T>> {

	// TODO : add configuration item to specify what we should resolve in the build method
	protected final Node<T> head;
	
	public TreeBuilder(Node<T> head) {
		this.head = head;
	}
	
}

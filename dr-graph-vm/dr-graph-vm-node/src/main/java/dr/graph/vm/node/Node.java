package dr.graph.vm.node;

import java.util.List;

import dr.common.logger.LazyLogger;

public interface Node<N> extends LazyLogger {

	Index getIndex();
	
	<N extends Node> List<N> getChildren();

	<N extends Node> N getParent();
	
}

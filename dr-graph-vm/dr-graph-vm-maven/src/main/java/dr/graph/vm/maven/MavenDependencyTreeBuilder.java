package dr.graph.vm.maven;

import dr.common.logger.LazyLogger;
import dr.common.struct.tree.Node;
import dr.common.struct.tree.Tree;
import dr.common.struct.tree.TreeBuilder;

//TODO : develop this class ... 
public class MavenDependencyTreeBuilder extends TreeBuilder<MavenDependency> implements LazyLogger{

	public MavenDependencyTreeBuilder(Node<MavenDependency> node , int lookUp , int lookDown) {
		super(node,lookUp,lookDown);
	}
	
	private MavenDependency getHead() {
		return (MavenDependency) head;
	}
	
	@Override
	protected Tree<MavenDependency> constructTree(){
		return new MavenDependencyTree(head);
	}

}

package dr.graph.vm.maven;

import dr.common.logger.LazyLogger;
import dr.common.struct.tree.Node;
import dr.common.struct.tree.Tree;
import dr.common.struct.tree.TreeBuilder;

//TODO : develop this class ... 
public class MavenDependencyTreeBuilder extends TreeBuilder<MavenDependency> implements LazyLogger{

	public MavenDependencyTreeBuilder(Node<MavenDependency> head) {
		super(head);
	}
	
	private MavenDependency getHead() {
		return (MavenDependency) head;
	}

	@Override
	public Tree<MavenDependency> build() {
//		List<MavenDependency> children = head.children();
//		children.stream().map(dependency -> {
//			log("address="+dependency.key());
//			if(!dependency.resolve()) {
//				dependency.patch(getHead()).children();
//				return dependency;
//			}
//			return null;
//		}).forEach( dependency -> log("dependency="+dependency) );
		return null;
	}

}

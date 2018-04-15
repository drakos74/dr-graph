package dr.graph.vm.maven;

import java.util.Map;

import dr.common.struct.tree.Node;
import dr.common.struct.tree.Tree;

public class MavenDependencyTree extends Tree<MavenDependency> {

	MavenDependencyTree(Node<MavenDependency> head) {
		super(head);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MavenDependency parent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Key, MavenDependency> children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Key key() {
		// TODO Auto-generated method stub
		return null;
	}

}

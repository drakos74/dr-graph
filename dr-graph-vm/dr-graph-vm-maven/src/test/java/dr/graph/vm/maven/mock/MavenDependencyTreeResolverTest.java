package dr.graph.vm.maven.mock;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import dr.common.struct.tree.Tree;
import dr.graph.vm.maven.MavenDependency;
import dr.graph.vm.maven.MavenDependencyKey;
import dr.graph.vm.maven.MavenDependencyTree;
import dr.graph.vm.maven.MavenDependencyTreeBuilder;

public class MavenDependencyTreeResolverTest extends FileSystemResolverBaseTest {

	private final String pom_path = "sample-poms-local/tree-structure";

	@Override
	protected String getPath() {
		return pom_path;
	}

	@Test
	public void testSimpleResolve() {

		MavenDependency dependency = createMavenDependency(
				MavenDependencyKey.fromString("sample.pom.group.id:tree-head::base-1:"));

		logger.info("pom=" + dependency.pom().getXml());

		Assert.assertNull(dependency.parent());
		List<MavenDependency> children = dependency.children().values().stream().collect(Collectors.toList());

		children.stream().map(c -> "child=" + c).forEach(logger::info);

		Assert.assertEquals(
				MavenDependencyKey.fromString("sample.pom.group.id:depth-1-child-1::0.0.0:"),
				children.get(0).key());

		Assert.assertEquals(
				MavenDependencyKey.fromString("sample.pom.group.id:depth-1-child-2::0.0.0:"),
				children.get(1).key());
		
		Assert.assertEquals(
				MavenDependencyKey.fromString("sample.pom.group.id:depth-1-child-3::0.0.0:"),
				children.get(2).key());
		

		List<MavenDependency> validChildren = children.stream().filter(MavenDependency::resolve)
				.collect(Collectors.toList());

		Assert.assertEquals(3, validChildren.size());

		Assert.assertTrue(validChildren.stream().map(MavenDependency::key).anyMatch(c -> c.equals(MavenDependencyKey
				.fromString("sample.pom.group.id:depth-1-child-1::0.0.0:"))));

		Assert.assertTrue(validChildren.stream().map(MavenDependency::key).anyMatch(c -> c.equals(MavenDependencyKey
				.fromString("sample.pom.group.id:depth-1-child-2::0.0.0:"))));
		
		Assert.assertTrue(validChildren.stream().map(MavenDependency::key).anyMatch(c -> c.equals(MavenDependencyKey
				.fromString("sample.pom.group.id:depth-1-child-3::0.0.0:"))));

	}
	
	@Test
	public void testTreeSelfHeadFinder() {
		MavenDependency dependency = createMavenDependency(
				MavenDependencyKey.fromString("sample.pom.group.id:tree-head::base-1:"));
		
		MavenDependencyTreeBuilder treeBuilder = new MavenDependencyTreeBuilder(dependency , 0 , 0);
		
		Tree tree = treeBuilder.build();
		
		Assert.assertEquals(dependency, tree.head());
		
		Assert.assertTrue(tree.head() == dependency);
		
	}
	
	@Test
	public void testTreeParentHeadFinder() {
		
		MavenDependency head = createMavenDependency(
				MavenDependencyKey.fromString("sample.pom.group.id:tree-head::0.0.0:"));
		
		MavenDependency dependency = createMavenDependency(
				MavenDependencyKey.fromString("sample.pom.group.id:depth-1-child-3::0.0.0:"));
		
		MavenDependencyTreeBuilder treeBuilder = new MavenDependencyTreeBuilder(dependency , 1 , 0);
		
		Tree tree = treeBuilder.build();
		
		
		
		Assert.assertEquals(head, tree.head());
		
	}

}

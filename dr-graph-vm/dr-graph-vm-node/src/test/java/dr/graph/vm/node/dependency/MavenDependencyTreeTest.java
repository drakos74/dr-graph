package dr.graph.vm.node.dependency;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.test.AbstractTestWrapper;

public class MavenDependencyTreeTest extends AbstractTestWrapper {

	private final static Logger logger = LoggerFactory.getLogger(MavenDependencyTreeTest.class);

	@Test(expected = IllegalArgumentException.class)
	public void testFailingResolve() {

		MavenDependencyIndex index = MavenDependencyIndex.fromString("com.google.guava:" + "guava");

		new MavenDependency(index).getChildren();

	}

	@Test
	public void testResolvableBehaviorWihtoutParent() {

		MavenDependency mavenDepedency = new MavenDependency(
				MavenDependencyIndex.fromString("com.google.guava:" + "guava"));

		Assert.assertFalse(mavenDepedency.resolves());

		Assert.assertFalse(mavenDepedency.patch(mavenDepedency));

	}

	@Test
	public void testResolveDependendies() {

		RunWebTest(() -> {
			MavenDependencyIndex index = MavenDependencyIndex.fromXml("<dependency>\n"
					+ "    <groupId>com.google.guava</groupId>\n" + "    <artifactId>guava</artifactId>\n"
					+ "    <version>24.0-jre</version>\n" + "</dependency>");

			MavenDependency dependency = new MavenDependency(index);

			logger.info("got " + dependency.getChildren().size() + " dependencies for " + dependency.getIndex());

			dependency.getChildren().stream().map(MavenDependency::getIndex).map(Object::toString)
					.forEach(logger::info);

			Assert.assertEquals(5, dependency.getChildren().size());
		});

	}

	@Test
	public void testResolveDependendiesInDepth() {

		RunWebTest(() -> {
			MavenDependencyIndex index = MavenDependencyIndex.fromXml("<dependency>\n"
					+ "    <groupId>com.google.guava</groupId>\n" + "    <artifactId>guava</artifactId>\n"
					+ "    <version>24.0-jre</version>\n" + "</dependency>");

			MavenDependency dependency = new MavenDependency(index);

			logger.info("got " + dependency.getChildren().size() + " dependencies for " + dependency.getIndex());

			List<MavenDependency> dependencies = dependency.getChildren().stream().flatMap(child -> {
				logger.info("find dependencies of ... child="+child.getIndex());
				List<MavenDependency> childDependencies = child.getChildren();
				logger.info("found "+childDependencies.size()+" dependencies");

				return childDependencies.stream();
			}).collect(Collectors.toList());

			dependencies.stream().map(d -> d.getIndex()).forEach(System.out::println);

			Assert.assertEquals(1, dependencies.size());
			
			Assert.assertEquals(1, dependencies.stream().map( d -> d.getIndex()).filter( i -> i.equals(MavenDependencyIndex.fromString("junit:junit::4.13-SNAPSHOT:test") ) ).count() );
			
		});

	}
	
	@Test
	public void testResolveFullDependencyTree() {

		// TODO : ...

	}

}

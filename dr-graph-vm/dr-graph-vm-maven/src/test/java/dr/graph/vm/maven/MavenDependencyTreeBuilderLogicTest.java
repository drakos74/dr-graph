package dr.graph.vm.maven;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.file.FileUtils;
import dr.common.struct.tree.Tree;
import dr.common.test.AbstractTestWrapper;
import dr.graph.vm.maven.resolver.MavenArtifactResolver;
import dr.graph.vm.maven.resolver.MavenDependenciesResolver;
import dr.graph.vm.source.Resolver;

public class MavenDependencyTreeBuilderLogicTest extends MavenDependencyTestBase {

	private final static Logger logger = LoggerFactory.getLogger(MavenDependencyTreeBuilderLogicTest.class);

	private static final String unresolvedDependencyKey = "com.google.guava:" + "guava";

	@Override
	protected Resolver<MavenDependencyKey, String> getPomResolver() {
		return new MavenArtifactResolver();
	}

	@Override
	protected Resolver<String, List<MavenDependencyKey>> getDependenciesResolver() {
		return new MavenDependenciesResolver();
	}

	@Test
	public void testNoResolve() {

		MavenDependencyKey index = MavenDependencyKey.fromString(unresolvedDependencyKey);

		MavenDependency dependency = createMavenDependency(index);

		Assert.assertFalse(dependency.resolve());

	}

	@Test(expected = UnresolvedDependencyException.class)
	public void testFailingResolve() {

		MavenDependencyKey index = MavenDependencyKey.fromString(unresolvedDependencyKey);

		MavenDependency dependency = createMavenDependency(index);

		// will throw Exception if it is not resolved ...
		dependency.children();

	}

	@Test
	public void testResolvableBehaviorWihtoutParent() {

		MavenDependency mavenDepedency = createMavenDependency(MavenDependencyKey.fromString(unresolvedDependencyKey));

		Assert.assertFalse(mavenDepedency.resolve());

		// Assert.assertTrue(mavenDepedency.patch(mavenDepedency) == null);

	}

	@Test
	public void testResolveIncompleteDependendies() {

		RunWebTest(() -> {
			MavenDependencyKey index = MavenDependencyKey
					.fromXml("<dependency>\n" + "    <groupId>com.google.guava</groupId>\n"
							+ "    <artifactId>guava</artifactId>\n" + "</dependency>");

			MavenDependency dependency = createMavenDependency(index);

			logger.info("dependency = " + dependency);

			// TODO : add some test method telling us that this cannot be resolved!!!
			// NOTE : no pom, no version .. so we dont know what we need to fetch from
			// artifactory here ...

			try {
				dependency.children();
				Assert.fail("No children can have been initialized " + this + " is incomplete");
			} catch (Exception e) {
				Assert.assertTrue(e instanceof UnresolvedDependencyException);
			}

			try {
				dependency.parent();
				Assert.fail("No parent info is available as " + this + " is incomplete");
			} catch (Exception e) {
				Assert.assertTrue(e instanceof UnresolvedDependencyException);
			}

		});

	}

	/**
	 * We can resolve this one ... we have a version at least ...
	 */
	@Test
	public void testResolveDependendyParents() {

		RunWebTest(() -> {
			MavenDependencyKey index = MavenDependencyKey.fromXml("<dependency>\n"
					+ "    <groupId>com.google.guava</groupId>\n" + "    <artifactId>guava</artifactId>\n"
					+ "    <version>24.0-jre</version>\n" + "</dependency>");

			MavenDependency dependency = createMavenDependency(index);

			// check our pom from artifactory with test file ...
			Assert.assertEquals(
					FileUtils.fromClassPath("/sample-copy-remote/google-guava-v24.0-jre-pom.xml", this.getClass())
							.orElseThrow(FileNotFoundException::new).replaceAll("\\s", ""),
					dependency.pom().getXml().replaceAll("\\s", ""));

			// check out pom with the other FilUtils parser
			Assert.assertEquals(
					FileUtils.fromFile("src/test/resources/sample-copy-remote/google-guava-v24.0-jre-pom.xml",
							this.getClass()).orElseThrow(FileNotFoundException::new).replaceAll("\\s", ""),
					dependency.pom().getXml().replaceAll("\\s", ""));

			// check our parent pom from artifactory with test file ...
			Assert.assertEquals(
					FileUtils
							.fromClassPath("/sample-copy-remote/google-guava-parent-v24.0-jre-pom.xml", this.getClass())
							.orElseThrow(FileNotFoundException::new).replaceAll("\\s", ""),
					dependency.parent().pom().getXml().replaceAll("\\s", ""));

			// check out parent pom with the other FilUtils parser
			Assert.assertEquals(
					FileUtils.fromFile("src/test/resources/sample-copy-remote/google-guava-parent-v24.0-jre-pom.xml",
							this.getClass()).orElseThrow(FileNotFoundException::new).replaceAll("\\s", ""),
					dependency.parent().pom().getXml().replaceAll("\\s", ""));

			// check our parent's parent pom from artifactory with test file ...
			Assert.assertEquals(
					FileUtils.fromClassPath("/sample-copy-remote/google-guava-parent-parent-v24.0-jre-pom.xml",
							this.getClass()).orElseThrow(FileNotFoundException::new).replaceAll("\\s", ""),
					dependency.parent().parent().pom().getXml().replaceAll("\\s", ""));

			// check out parent's parent pom with the other FilUtils parser
			Assert.assertEquals(
					FileUtils.fromFile(
							"src/test/resources/sample-copy-remote/google-guava-parent-parent-v24.0-jre-pom.xml",
							this.getClass()).orElseThrow(FileNotFoundException::new).replaceAll("\\s", ""),
					dependency.parent().parent().pom().getXml().replaceAll("\\s", ""));

			// check our head parent pom from artifactory with test file ...
			Assert.assertNull(dependency.parent().parent().parent());

		});

	}

	/**
	 * We can resolve this one ... we have a version at least ...
	 */
	@Test
	public void testResolveDependendies() {

		RunWebTest(() -> {
			MavenDependencyKey index = MavenDependencyKey.fromXml("<dependency>\n"
					+ "    <groupId>com.google.guava</groupId>\n" + "    <artifactId>guava</artifactId>\n"
					+ "    <version>24.0-jre</version>\n" + "</dependency>");

			MavenDependency dependency = createMavenDependency(index);

			logger.info("got " + dependency.children().size() + " dependencies for " + dependency.key());

			dependency.children().values().stream().map(MavenDependency::key).map(Object::toString)
					.forEach(logger::info);

			Assert.assertEquals(5, dependency.children().size());

			// check that children have valid keys ...

			Assert.assertTrue(dependency.children().values().stream().map(MavenDependency::key).filter(k -> {
				// NOTE : we should be able to resolve these keys ... !!!
				return ((MavenDependencyKey) k).resolve();
			}).count() == 5);

		});

	}

	@Test
	public void testResolveDependendiesInDepth() {

		RunWebTest(() -> {
			MavenDependencyKey index = MavenDependencyKey.fromXml("<dependency>\n"
					+ "    <groupId>com.google.guava</groupId>\n" + "    <artifactId>guava</artifactId>\n"
					+ "    <version>24.0-jre</version>\n" + "</dependency>");

			MavenDependency dependency = createMavenDependency(index);

			logger.info("got " + dependency.children().size() + " dependencies for " + dependency.key());

			List<MavenDependency> dependencies = dependency.children().values().stream().flatMap(child -> {
				logger.info("find dependencies of ... child=" + child.key());
				Collection<MavenDependency> childDependencies = child.children().values();
				logger.info("found " + childDependencies.size() + " dependencies");
				return childDependencies.stream();
			}).collect(Collectors.toList());

			dependencies.stream().map(d -> d.key()).forEach(System.out::println);

			Assert.assertEquals(1, dependencies.size());

			Assert.assertEquals(1, dependencies.stream().map(d -> d.key())
					.filter(i -> i.equals(MavenDependencyKey.fromString("junit:junit::4.13-SNAPSHOT:test"))).count());

		});

	}

	// TODO : complete test
//	@Test
//	public void testResolveDependendencyTreeInDepth() {
//
//		RunWebTest(() -> {
//			MavenDependencyKey index = MavenDependencyKey.fromXml("<dependency>\n"
//					+ "    <groupId>com.google.guava</groupId>\n" + "    <artifactId>guava</artifactId>\n"
//					+ "    <version>24.0-jre</version>\n" + "</dependency>");
//
//			MavenDependency dependency = createMavenDependency(index);
//
//			logger.info("got " + dependency.children().size() + " dependencies for " + dependency.key());
//
//			MavenDependencyTreeBuilder treeBuilder = new MavenDependencyTreeBuilder(dependency);
//
//			Tree<MavenDependency> tree = treeBuilder.build();
//
//			List<MavenDependency> dependencies = dependency.children().values().stream().flatMap(child -> {
//				logger.info("find dependencies of ... child=" + child.key());
//				Collection<MavenDependency> childDependencies = child.children().values();
//				logger.info("found " + childDependencies.size() + " dependencies");
//				return childDependencies.stream();
//			}).collect(Collectors.toList());
//
//			dependencies.stream().map(d -> d.key()).forEach(System.out::println);
//
//			Assert.assertEquals(1, dependencies.size());
//
//			Assert.assertEquals(1, dependencies.stream().map(d -> d.key())
//					.filter(i -> i.equals(MavenDependencyKey.fromString("junit:junit::4.13-SNAPSHOT:test"))).count());
//
//		});
//
//	}

	@Test
	public void testResolveFullDependencyTree() {

		// TODO : ...

	}

}

package dr.graph.vm.maven.local;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import dr.common.file.FileUtils;
import dr.graph.vm.maven.MavenDependency;
import dr.graph.vm.maven.MavenDependencyKey;
import dr.graph.vm.maven.MavenDependencyTestBase;
import dr.graph.vm.maven.resolver.MavenDependenciesResolver;
import dr.graph.vm.source.Resolver;

public abstract class FileSystemResolverBaseTest extends MavenDependencyTestBase {

	protected abstract String getPath();
	
	@Override
	protected Resolver<MavenDependencyKey, String> getPomResolver() {
		return (k) -> {
			try {
				String pom = FileUtils.fromFile("src/test/resources/" + getPath() + "/" + k.getArtifactId() + ".xml",
						this.getClass()).get();
				return pom;
			} catch (IOException | URISyntaxException e) {
				// Assert.fail("Could not resolve ... "+k);
			}
			return null;
		};
	}

	@Override
	protected Resolver<String, List<MavenDependencyKey>> getDependenciesResolver() {
		return new MavenDependenciesResolver();
	}
	
	/**
	 * NOTE : Every test starts with the base pom : base-pom.xml
	 * and needs to resolve the basic artifacts as shown below ...
	 * what is supposed to change amongst tests, is the way versions are resolved for the poms ... 
	 * */
	@Test
	public void testSimpleResolve() {

		MavenDependency dependency = createMavenDependency(
				MavenDependencyKey.fromString("sample.pom.group.id:base-pom::base-1:"));

		logger.info("pom=" + dependency.pom().getXml());

		logger.info("parent=" + dependency.parent().pom().getXml());

		List<MavenDependency> children = dependency.children().values().stream().collect(Collectors.toList());

		children.stream().map(c -> "child=" + c).forEach(logger::info);
		
		Assert.assertEquals(MavenDependencyKey.fromString("sample.pom.group.id:child-pom-as-dependency::child-dependency-version:"),
				children.get(0).key());
		
		Assert.assertEquals(MavenDependencyKey.fromString("sample.pom.group.id:child-pom-no-version:::"),
				children.get(1).key());
		
		Assert.assertEquals(MavenDependencyKey.fromString("sample.pom.group.id:child-pom-from-property::child-property-version:"),
				children.get(2).key());
		
		List<MavenDependency> validChildren = children.stream().filter(MavenDependency::resolve).collect(Collectors.toList());
		
		// note we have one dependency that does not resolve ... 
		Assert.assertEquals( 2, validChildren.size());
		
		Assert.assertTrue(validChildren.stream().map( MavenDependency::key ).anyMatch( c -> c.equals(MavenDependencyKey.fromString("sample.pom.group.id:child-pom-from-property::child-property-version:")) ));

		Assert.assertTrue(validChildren.stream().map( MavenDependency::key ).anyMatch( c -> c.equals(MavenDependencyKey.fromString("sample.pom.group.id:child-pom-as-dependency::child-dependency-version:")) ));

	}
	
}

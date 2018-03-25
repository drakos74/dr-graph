package dr.graph.vm.maven;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.file.FileUtils;
import dr.common.test.AbstractTestWrapper;
import dr.graph.vm.maven.resolver.MavenDependenciesResolver;
import dr.graph.vm.source.Resolver;

public abstract class MavenDependencyResolvableTestBase extends AbstractTestWrapper {

	protected final static Logger logger = LoggerFactory.getLogger(MavenDependencyResolvableTest.class);
	
	protected final Resolver<MavenDependencyKey, String> pomResolver(String pom_path){
		return (k) -> {
			try {
				String pom = FileUtils.fromFile("src/test/resources/" + pom_path + "/" + k.getArtifactId() + ".xml",
						this.getClass());
				return pom;
			} catch (IOException | URISyntaxException e) {
				// Assert.fail("Could not resolve ... "+k);
			}
			return null;
		};
	} 

	protected MavenDependency createMavenDependency(final MavenDependencyKey key , String pom_path) {
		return new MavenDependency.Builder().withPomResolver(pomResolver(pom_path))
				.withDependencyResolver(new MavenDependenciesResolver()).build(key);
	}
	
	public void testSimpleResolve(String pom_path) {
		
		MavenDependency dependency = createMavenDependency( 
				MavenDependencyKey.fromString("sample.pom.group.id:base-pom::base-1:"),
				pom_path);
		
		logger.info("pom="+dependency.pom().getXml());
		
		logger.info("parent="+dependency.parent().pom().getXml());
		
		List<MavenDependency> children = dependency.children();
		
		children.stream().map( c -> "child="+c).forEach(logger::info);
		
		Assert.assertEquals(MavenDependencyKey.fromString("sample.pom.group.id:child-pom-1::child-1:"),children.get(0).key());
		
	}

}

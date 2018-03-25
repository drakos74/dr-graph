package dr.graph.vm.maven;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.test.AbstractTestWrapper;
import dr.graph.vm.maven.resolver.MavenArtifactResolver;

public class MavenDependencyKeyTest extends AbstractTestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(MavenDependencyKeyTest.class);

	@Test
	public void testMavenDependencyIndexResolve() {
		
		logger.info("running ... testMavenDependencyIndexResolve");
		
		MavenArtifactResolver resolver = new MavenArtifactResolver();
		
		RunWebTest( () -> {
			resolver.resolve( MavenDependencyKey.fromString(
					"junit" + ":" +
					"junit" + ":" +
					"jar" + ":" +
					"4.12") );
		});
		
	}
	
	@Test
	public void testMavenDependencyIndexCreate() {
		RunTest( () -> {
			MavenDependencyKey index = MavenDependencyKey.fromXml("<dependency>" + 
					"			<groupId>dr.graph.vm</groupId>" + 
					"			<artifactId>dr-common</artifactId>" + 
					"			<version>${current.version}</version>" + 
					"		</dependency>");
			
			logger.info("index="+index);
		});
		
		
	}
	
}

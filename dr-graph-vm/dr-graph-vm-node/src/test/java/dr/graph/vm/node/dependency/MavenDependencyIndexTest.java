package dr.graph.vm.node.dependency;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.test.AbstractTestWrapper;
import dr.graph.vm.node.dependency.source.MavenArtifactResolver;
import dr.graph.vm.source.GenericResolverException;

public class MavenDependencyIndexTest extends AbstractTestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(MavenDependencyIndexTest.class);

	@Test
	public void testMavenDependencyIndexResolve() {
		
		logger.info("running ... testMavenDependencyIndexResolve");
		
		MavenArtifactResolver resolver = new MavenArtifactResolver();
		
		RunWebTest( () -> {
			resolver.resolve( MavenDependencyIndex.fromString(
					"junit" + ":" +
					"junit" + ":" +
					"jar" + ":" +
					"4.12") );
		});
		
	}
	
	@Test
	public void testMavenDependencyIndexCreate() {
		RunTest( () -> {
			MavenDependencyIndex index = MavenDependencyIndex.fromXml("<dependency>" + 
					"			<groupId>dr.graph.vm</groupId>" + 
					"			<artifactId>dr-common</artifactId>" + 
					"			<version>${current.version}</version>" + 
					"		</dependency>");
			
			logger.info("index="+index);
		});
		
		
	}
	
}

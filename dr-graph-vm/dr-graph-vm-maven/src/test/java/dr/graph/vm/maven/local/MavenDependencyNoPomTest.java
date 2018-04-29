package dr.graph.vm.maven.local;

import org.junit.Test;

import dr.graph.vm.maven.MavenDependency;
import dr.graph.vm.maven.MavenDependencyKey;

public class MavenDependencyNoPomTest  extends FileSystemResolverBaseTest {

	private final String pom_path = "sample-poms-local/no-pom";

	@Override
	protected String getPath() {
		return pom_path;
	}
	
	// TODO : either throw the error, or decide how to handle this case ... 
	@Override
	@Test(expected = IllegalArgumentException.class)
	public void testSimpleResolve() {
		
		MavenDependency head = createMavenDependency(
				MavenDependencyKey.fromString("sample.pom.group.id:tree-head::0.0.0:"));
		
	}
}

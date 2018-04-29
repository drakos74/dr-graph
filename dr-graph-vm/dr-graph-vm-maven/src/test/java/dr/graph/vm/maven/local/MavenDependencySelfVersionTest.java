package dr.graph.vm.maven.local;

import org.junit.Test;

import dr.graph.vm.maven.MavenDependency;
import dr.graph.vm.maven.MavenDependencyKey;

public class MavenDependencySelfVersionTest extends FileSystemResolverBaseTest {

	private final String pom_path = "sample-poms-local/self-version";

	@Override
	protected String getPath() {
		return pom_path;
	}
	
	@Override
	@Test(expected = IllegalArgumentException.class)
	public void testSimpleResolve() {
		
		MavenDependency head = createMavenDependency(
				MavenDependencyKey.fromString("sample.pom.group.id:tree-head::ANY_WRONG_VERSION:"));
		
	}
}

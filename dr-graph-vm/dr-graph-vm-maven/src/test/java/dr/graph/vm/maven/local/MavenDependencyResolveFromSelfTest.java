package dr.graph.vm.maven.local;

public class MavenDependencyResolveFromSelfTest extends FileSystemResolverBaseTest {

	private final String pom_path = "sample-poms-local/child-version-in-self";

	@Override
	protected String getPath() {
		return pom_path;
	}
	
}

package dr.graph.vm.maven.local;

public class MavenDependencyResolveFromParentTest extends FileSystemResolverBaseTest {

	private final String pom_path = "sample-poms-local/child-version-in-parent";

	@Override
	protected String getPath() {
		return pom_path;
	}
	
}

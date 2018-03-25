package dr.graph.vm.maven;

import org.junit.Test;

public class MavenDependencyResolvableTest extends MavenDependencyResolvableTestBase{

	protected final static String sample_poms_path_parent_plain_xml = "sample-poms-child-versions-in-parent";
	protected final static String sample_poms_path_self_property = "sample-poms-child-versions-as-property";
	protected final static String sample_poms_path_parent_property = "sample-poms-child-version-in-parent-as-property";
	@Test
	public void testResolveFromParentAsPlainXml() {
		testSimpleResolve(sample_poms_path_parent_plain_xml);
	}
	
	@Test
	public void testResolveFromSelfWithProperty() {
		testSimpleResolve(sample_poms_path_self_property);
	}
	
	@Test
	public void testResolveFromParentWithProperty() {
		testSimpleResolve(sample_poms_path_parent_property);
	}
	
}

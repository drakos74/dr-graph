package dr.graph.vm.node.url;

import dr.graph.vm.node.dependency.MavenDependencyIndex;

public class URLBuilder {

	// TODO : specify according to repo setup
	private final static String urlPrefix = "http://repo.maven.apache.org/maven2/";

	public String build(MavenDependencyIndex index) {

		StringBuilder stringBuilder = new StringBuilder(urlPrefix);

		stringBuilder
		.append(index.getGroupId().replaceAll("\\.", "/"))
		.append("/")
		.append(index.getArtifactId())
		.append("/")
		.append(index.getVersion())
		.append("/")
		.append(index.getArtifactId())
		.append("-")
		.append(index.getVersion())
		.append(".pom");

		return stringBuilder.toString();

	}

}
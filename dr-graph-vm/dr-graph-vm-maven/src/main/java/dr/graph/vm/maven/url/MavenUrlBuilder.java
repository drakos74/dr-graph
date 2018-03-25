package dr.graph.vm.maven.url;

import dr.graph.vm.maven.MavenDependencyKey;
import dr.graph.vm.source.http.UrlBuilder;

public class MavenUrlBuilder implements UrlBuilder<MavenDependencyKey> {

	// TODO : specify according to repo setup
	private final static String urlPrefix = "http://repo.maven.apache.org/maven2/";
	
	@Override
	public String build(MavenDependencyKey key) {

		StringBuilder stringBuilder = new StringBuilder(urlPrefix);

		stringBuilder
		.append(key.getGroupId().replaceAll("\\.", "/"))
		.append("/").append(key.getArtifactId())
		.append("/").append(key.getVersion())
		.append("/").append(key.getArtifactId())
		.append("-")
		.append(key.getVersion()).append(".pom");
		return stringBuilder.toString();
	}

}

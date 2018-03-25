package dr.graph.vm.maven.resolver;

import dr.graph.vm.maven.MavenDependencyKey;
import dr.graph.vm.maven.url.MavenUrlBuilder;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;
import dr.graph.vm.source.http.UrlBuilder;
import dr.graph.vm.source.http.UrlResolver;

public class MavenArtifactResolver implements Resolver<MavenDependencyKey, String> {
    
	private Resolver<String, String> urlResolver = new UrlResolver(new UrlResolver.Configuration()
			.withConnType(UrlResolver.ConnectionType.HTTP).withReqType(UrlResolver.RequestType.GET));

	private final UrlBuilder<MavenDependencyKey> urlBuilder;
	
	private String url;
	private String html;

	public MavenArtifactResolver() {
		this(new MavenUrlBuilder());
	}
	
	public MavenArtifactResolver(UrlBuilder<MavenDependencyKey> urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	public String resolve(MavenDependencyKey index) throws GenericResolverException {
		log("resolving index="+index);
		url = urlBuilder.build(index);
		log("url="+url);
		html = urlResolver.resolve(url);
		log("html="+html);
		return html;
	}

	public boolean isDebug() {
		return debug;
	}

	public Resolver<String, String> getUrlResolver() {
		return urlResolver;
	}

	public UrlBuilder<MavenDependencyKey> getUrlBuilder() {
		return urlBuilder;
	}

	public String getUrl() {
		return url;
	}

	public String getHtml() {
		return html;
	}
	
}

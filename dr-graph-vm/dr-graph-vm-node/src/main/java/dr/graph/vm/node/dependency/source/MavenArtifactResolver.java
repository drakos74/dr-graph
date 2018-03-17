package dr.graph.vm.node.dependency.source;

import dr.graph.vm.node.dependency.MavenDependencyIndex;
import dr.graph.vm.node.url.URLBuilder;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;
import dr.graph.vm.source.http.UrlResolver;

public class MavenArtifactResolver implements Resolver<MavenDependencyIndex, String> {
    
	private Resolver<String, String> urlResolver = new UrlResolver(new UrlResolver.Configuration()
			.withConnType(UrlResolver.ConnectionType.HTTP).withReqType(UrlResolver.RequestType.GET));

	private final URLBuilder urlBuilder;
	
	private String url;
	private String html;

	public MavenArtifactResolver() {
		this(new URLBuilder());
	}
	
	public MavenArtifactResolver(URLBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	public String resolve(MavenDependencyIndex index) throws GenericResolverException {
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

	public URLBuilder getUrlBuilder() {
		return urlBuilder;
	}

	public String getUrl() {
		return url;
	}

	public String getHtml() {
		return html;
	}
	
}

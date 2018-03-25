package dr.graph.vm.source.http;

@FunctionalInterface
public interface UrlBuilder<K> {

	String build(K key);
	
}

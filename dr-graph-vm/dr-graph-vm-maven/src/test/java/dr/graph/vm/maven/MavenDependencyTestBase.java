package dr.graph.vm.maven;

import java.util.List;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.test.AbstractTestWrapper;
import dr.graph.vm.source.Resolver;

/**
 * emulates Tree Builder internal logic
 * */
public abstract class MavenDependencyTestBase extends AbstractTestWrapper {

	protected final static Logger logger = LoggerFactory.getLogger(MavenDependencyTestBase.class);
	
	private Resolver<MavenDependencyKey, String> pomResolver;
	private Resolver<String, List<MavenDependencyKey>>  dependenciesResolver;
	
	protected abstract Resolver<MavenDependencyKey, String> getPomResolver();
	protected abstract Resolver<String, List<MavenDependencyKey>> getDependenciesResolver();
	
	@Before
	public void before() {
		pomResolver = getPomResolver();
		dependenciesResolver = getDependenciesResolver();
	}

	protected MavenDependency createMavenDependency(final MavenDependencyKey key) {
		return new MavenDependency
				.Builder()
				.withPomResolver(pomResolver)
				.withDependencyResolver(dependenciesResolver)
				.build(key);
	}

}

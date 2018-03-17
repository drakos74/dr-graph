package dr.graph.vm.source.http;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.test.AbstractTestWrapper;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.http.UrlResolver;

public class UrlResolverTest extends AbstractTestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(UrlResolverTest.class);

	@Test(expected = GenericResolverException.class)
	public void testUrlRetrieverBadUrl() throws GenericResolverException {
		new UrlResolver(new UrlResolver.Configuration().withConnType(UrlResolver.ConnectionType.HTTP)
				.withReqType(UrlResolver.RequestType.GET)).resolve("http://www.bad.url.com");

	}
	
	@Test
	public void testUrlRetriever() {
		RunWebTest( () -> {
			logger.info("running ... testUrlRetriever");
			String result = new UrlResolver(new UrlResolver.Configuration().withConnType(UrlResolver.ConnectionType.HTTP)
					.withReqType(UrlResolver.RequestType.GET)).resolve("http://www.google.com");
			logger.info(result);
			
			Assert.assertTrue(result.contains("google"));
		});

	}

}

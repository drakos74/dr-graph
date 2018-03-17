package dr.graph.vm.source.http.incubator;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.http.incubator.UrlResolver;

public class UrlResolverTest {

    private static final Logger logger = LoggerFactory.getLogger(UrlResolverTest.class);

	@Test(expected = GenericResolverException.class)
	@Ignore
	public void testUrlRetrieverBadUrl() throws GenericResolverException {
		new UrlResolver().resolve("http://www.bad.url.com");

	}
	
	@Test
	@Ignore
	public void testUrlRetriever() throws GenericResolverException {
		logger.info("running ... testUrlRetriever");
		String result = new UrlResolver().resolve("http://www.google.com");
		System.out.println(result);
		
		Assert.assertTrue(result.contains("google"));

	}

}

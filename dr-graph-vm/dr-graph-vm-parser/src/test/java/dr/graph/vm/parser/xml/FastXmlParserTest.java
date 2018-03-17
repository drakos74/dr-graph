package dr.graph.vm.parser.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.graph.vm.parser.GenericParseException;
import dr.graph.vm.parser.string.FastStringParser;

public class FastXmlParserTest {

	private static final Logger logger = LoggerFactory.getLogger(FastXmlParserTest.class);

	@Test
	public void testParsingMavenDependencies() throws IOException, GenericParseException {
		
		Path path = Paths.get("pom.xml");
		
		logger.info(path.toString());
		
		List<String> lines = Files.readAllLines(Paths.get("pom.xml"));
		
		FastStringParser parser = new FastStringParser("<dependency>", "</dependency>");

		Set<String> dependencies = parser.parse(String.join("", lines));
		
		dependencies.stream().forEach(System.out::println);
		
		Assert.assertEquals(1, dependencies.size());
		
		Assert.assertTrue(dependencies.iterator().next().contains("dr-common"));
		
	}
	
}

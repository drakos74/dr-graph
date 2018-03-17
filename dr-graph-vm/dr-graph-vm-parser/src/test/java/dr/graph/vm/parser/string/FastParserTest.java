package dr.graph.vm.parser.string;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.graph.vm.parser.GenericParseException;

public class FastParserTest {

	private static final Logger logger = LoggerFactory.getLogger(FastParserTest.class);

	@Test
	public void testFastParserSimple() throws GenericParseException {

		String randomString = "nojdfsnfidsafbhsdoabfhuods";
		String startString = "asd";
		String endString = "jkl";

		FastStringParser parser = new FastStringParser(startString, endString);

		Set<String> results = parser.parse(startString + randomString + endString);

		Assert.assertEquals(1, results.size());
		
		results.stream().forEach(logger::info);
		
		logger.info(randomString+" => "+results.size());

		Assert.assertTrue(results.contains(randomString));

	}

	@Test
	public void testFastParserMulti() throws GenericParseException {

		String randomString1 = "nojdfsnfidsafbhsdoabfhuods";
		String randomString2 = "8475390574398057438905743290";
		String randomString3 = "jihvrubvirtyubvruvberoubveir";

		String startString = "asd";
		String endString = "jkl";

		FastStringParser parser = new FastStringParser(startString, endString);

		Set<String> results = parser
				.parse(startString + randomString1 + endString + "wercweqrwrcewcrweqwe" + startString + randomString2
						+ endString + "dsfewfewrgrerwgrewrew" + startString + randomString3 + endString);

		Assert.assertEquals(3, results.size());
		
		results.stream().forEach(logger::info);

		Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[] { randomString1,randomString2,randomString3 })), results);

	}
	
	//TODO : this test is too much for now ... But we need to fix this at some point !
	@Test
	@Ignore
	public void testFastParserNested() throws GenericParseException {

		String a = "a";
		String b = "b";
		String c = "c";

		String startString = "[";
		String endString = "]";

		FastStringParser parser = new FastStringParser(startString, endString);

		Set<String> results = parser
				.parse("[[a]b][[[c]]][b][a[b[c]b]a]");
		
		logger.info("results="+results.size());
		
		Assert.assertEquals(9, results.size());

		results.stream().forEach(logger::info);

//		Assert.assertEquals(new HashSet<String>(Arrays.asList(new String[] { randomString1,randomString2,randomString3 })), results);

	}

}

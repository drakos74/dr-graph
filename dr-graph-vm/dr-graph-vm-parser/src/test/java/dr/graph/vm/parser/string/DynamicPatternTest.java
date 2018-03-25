package dr.graph.vm.parser.string;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import dr.common.test.AbstractTestWrapper;

public class DynamicPatternTest extends AbstractTestWrapper {

	@Test
	public void testSimpleDynamicPattern() {

		RunTest(() -> {
			String name = "name";

			String text = "123<a>" + name + "</a>"+
			"bla<bla>bla</bla>";

			DynamicPattern pattern = new DynamicPattern();

			for (int i = 0; i < text.length(); i++) {

				Optional<Boolean> result = pattern.test(text.charAt(i));

				logger.info("Got " + result + " at " + i + " = " + text.charAt(i) + " of " + text.substring(0,i+1));

				switch (i) {
				// at 123
				case 0:
				case 1:
				case 2:
					Assert.assertEquals("at " + i, Optional.empty(), result);
					Assert.assertNull("name should be null at " + i, pattern.name);
					Assert.assertNull("pattern should be null at " + i, pattern.closingPattern);
					break;
				// at <a>
				case 3:
				case 4:
					Assert.assertEquals("at " + i, Optional.of(false), result);
					Assert.assertNull("pattern should be null at " + i, pattern.closingPattern);
					break;
				// at name
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
					Assert.assertEquals("at " + i, Optional.of(false), result);
					Assert.assertEquals(1, pattern.name.length());
					Assert.assertEquals("a", pattern.name.toString());
					Assert.assertNotNull("pattern should be null at " + i, pattern.closingPattern);
					Assert.assertEquals("</a>", pattern.closingPattern.get());
					break;
				case 13:
					Assert.assertEquals("at " + i, Optional.of(true), result);
					// we should reset here ...
					Assert.assertNull("name should be null at " + i, pattern.name);
					Assert.assertNull("pattern should be null at " + i, pattern.closingPattern);
					break;
				case 14:
				case 15:
				case 16:
					Assert.assertEquals("at " + i, Optional.empty(), result);
					Assert.assertNull("name should be null at " + i, pattern.name);
					Assert.assertNull("pattern should be null at " + i, pattern.closingPattern);
					break;
				case 17:
				case 18:
				case 19:
				case 20:
					Assert.assertEquals("at " + i, Optional.of(false), result);
					// check how name expands ... 
					Assert.assertEquals("at " + i,i - 17, pattern.name.length());
					Assert.assertNull("pattern should be null at " + i, pattern.closingPattern);
					break;
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
				case 26:
				case 27:
				case 28:
				case 29:
					Assert.assertEquals("at " + i,3, pattern.name.length());
					Assert.assertEquals("bla", pattern.name.toString());
					Assert.assertNotNull("pattern should not be null at " + i, pattern.closingPattern);
					Assert.assertEquals("</bla>", pattern.closingPattern.get());
					break;
				case 30:
					Assert.assertEquals("at " + i, Optional.of(true), result);
					// we should reset here ...
					Assert.assertNull("name should be null at " + i, pattern.name);
					Assert.assertNull("pattern should be null at " + i, pattern.closingPattern);
					break;
				default:
					Assert.fail("Test case is not covered="+i);

				}

			}
		});

	}

}

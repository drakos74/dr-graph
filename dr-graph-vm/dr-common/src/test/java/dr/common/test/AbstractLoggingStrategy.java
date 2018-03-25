package dr.common.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLoggingStrategy{

	protected static Logger logger = LoggerFactory.getLogger(AbstractLoggingStrategy.class);
	
	static
	{
		System.setProperty("debug", "true");
	}
	
}

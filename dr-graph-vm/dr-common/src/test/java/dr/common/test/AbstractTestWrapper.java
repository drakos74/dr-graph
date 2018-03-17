package dr.common.test;

import org.junit.Assert;

public abstract class AbstractTestWrapper extends AbstractLoggingStrategy {

	protected void RunTest(TesterModule module) {
		try {
			module.execute();
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
			Assert.fail(e.getMessage());
		}
	}
	
	protected void RunWebTest(TesterModule module) {
		try {
			module.execute();
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
			Assert.fail(e.getMessage()+". "+
			"NOTE: This test might have failed because of missing internet connection."+
			" Some tests are currently dependent on this, due to laziness to create stubs :P."+
			" If it's not too much trouble, @Ignore this test and continue ... ");
		}
	}
	
	@FunctionalInterface
	protected interface TesterModule{
		
		void execute() throws Exception;
		
	} 
	
}

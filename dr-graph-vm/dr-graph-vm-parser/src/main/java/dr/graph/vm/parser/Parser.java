package dr.graph.vm.parser;

import dr.common.logger.LazyLogger;

@FunctionalInterface
public interface Parser<O,I> extends LazyLogger{
	/**
	 * implement also with BufferedReader
	 * */
	O parse(I input) throws GenericParseException;
	
}

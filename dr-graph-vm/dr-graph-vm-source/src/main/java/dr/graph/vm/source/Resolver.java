package dr.graph.vm.source;

import dr.common.logger.LazyLogger;

@FunctionalInterface
public interface Resolver<A,T> extends LazyLogger {

	T resolve(A id) throws GenericResolverException;
	
}

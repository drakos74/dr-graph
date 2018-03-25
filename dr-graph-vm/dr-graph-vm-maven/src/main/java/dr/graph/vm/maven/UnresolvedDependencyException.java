package dr.graph.vm.maven;

public class UnresolvedDependencyException extends RuntimeException{

	public UnresolvedDependencyException() {
		super("This object has not been resolved. Please make sure resolve and patch have been called.");
	}
	
}

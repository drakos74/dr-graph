package dr.common;

@FunctionalInterface
public interface Patchable<P> {

	P patch(P patch);
	
}

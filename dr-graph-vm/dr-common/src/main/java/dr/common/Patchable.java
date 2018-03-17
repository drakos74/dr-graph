package dr.common;

@FunctionalInterface
public interface Patchable {

	<P extends Patchable> boolean  patch(P patch);
	
}

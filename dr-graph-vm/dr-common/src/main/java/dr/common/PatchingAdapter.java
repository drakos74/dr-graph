package dr.common;

public interface PatchingAdapter<T> extends Resolvable , Patchable<T> {

	default boolean resolve(T t) {
		return resolve() || patch(t) != null;
	}
	
}

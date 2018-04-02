package dr.common.db;

import java.io.Serializable;

public interface DB<K,V> {

	V put(K k,V v);
	
	V get(K k);
	
	int size();
	
	@FunctionalInterface
	static interface Key<I> extends Serializable {
		
		I key();
		
	}
	
	@FunctionalInterface
	static interface Value<H> extends Serializable{
		
		H address(); 
		
	}
	
	static interface KeyValue<I,H> extends Key<I> , Value<H>{}
	
}

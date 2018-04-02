package dr.common.db.impl;

import java.util.HashMap;
import java.util.Map;

import dr.common.db.DB;

public class CachedMapDB<K,V> implements DB<K,V> {

	private final Map<K,V> map = new HashMap<>();
	
	@Override
	public V put(K k, V v) {
		return map.put(k, v);
	}

	@Override
	public V get(K k) {
		return map.get(k);
	}

	@Override
	public int size() {
		return map.size();
	}

	// TODO :implement metrics
	
}

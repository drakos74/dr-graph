package dr.common.db;

public abstract class LinearDB<I,K extends DB.Key<I>, H , V extends DB.Value<H>> implements DB<K,V> {

	private final DB<K,V> storeProvider;
	
	public LinearDB(DB<K,V> storeProvider) {
		this.storeProvider = storeProvider;
	}
	
	@Override
	public V put(K k , V v) {
		return storeProvider.put(k, v);
	}
	
	@Override
	public V get(K k) {
		return storeProvider.get(k);
	}
	
	@Override
	public int size() {
		return storeProvider.size();
	}
	
}

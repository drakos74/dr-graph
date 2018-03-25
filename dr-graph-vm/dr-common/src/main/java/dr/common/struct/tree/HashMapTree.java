package dr.common.struct.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

public class HashMapTree extends Tree<HashMapTree> {

	private final HashMapTree parent;
	private final Node.Index index;
	private final Map<Node.Key, HashMapTree> children = new ConcurrentHashMap<>();

	public HashMapTree(HashMapTree parent, Node.Index index) {
		super();
		this.parent = parent;
		this.index = index;
	}

	@Override
	public HashMapTree parent() {
		return parent;
	}

	@Override
	public List<HashMapTree> children() {
		return Collections.unmodifiableList(new ArrayList<>(children.values()));
	}

	@Override
	public int depth() {
		return index.depth();
	}

	@Override
	public Key[] address() {
		return index.address();
	}

	@Override
	public int index() {
		return index.index();
	}

	@Override
	public boolean add(HashMapTree child) {
		return children.put(child.key(), child) == null;
	}

	@Override
	public boolean remove(Key key) {
		return children.remove(key) != null;
	}

	@Override
	public Key key() {
		return index.key();
	}

	@Override
	public int granularity() {
		return index.granularity();
	}

	@Override
	public HashMapTree get(Key key) {
		return children.get(key);
	}

	// TODO : test this method 
	@Override
	public HashMapTree get(Key[] address, Key key) {
		return Arrays.stream(address).collect(Collector.<Key, AtomicReference<HashMapTree>>of(
				() -> new AtomicReference<HashMapTree>(this), 
				(result, value) -> {
					result.set((HashMapTree) result.get().get(value));	
				}, 
				(result1 , result2) -> {
					throw new IllegalStateException("IllegalState");
				} , 
				Characteristics.CONCURRENT) )
				.get();
	}

}

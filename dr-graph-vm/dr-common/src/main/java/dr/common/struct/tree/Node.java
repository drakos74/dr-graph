package dr.common.struct.tree;

import java.util.List;
import java.util.Map;

public interface Node<T> {

	T parent();
	
	Map<Key,T> children();

	boolean add(T child);

	T get(Key key);
	
	T get(Key[] address,Key key);

	boolean remove(Key key);

	public static interface Index {

		// key + address = unique identifier
		// single string
		Key key();

		// dot delimited address space from root of outer structure
		Key[] address();

		// outer structure depth
		int depth();

		// outer structure index at same depth
		int index();

		// outer structure number of Nodes at same depth
		int granularity();

		public static Index HEAD() {
			return new Node.Index() {

				@Override
				public Key key() {
					return new Key() {
					};
				}

				@Override
				public Key[] address() {
					return new Key[0];
				}

				@Override
				public int depth() {
					return 0;
				}

				@Override
				public int index() {
					return 0;
				}

				@Override
				public int granularity() {
					return 0;
				}
			};
		}
	}

	static interface Key {
	}
	
}

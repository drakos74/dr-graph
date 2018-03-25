package dr.common.struct.tree;

/**
 * abstract class to handle tree cache and navigation
 * */
public abstract class Tree<T> implements Node<T> , Node.Index{

	@Override
	public Key[] address() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int depth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int index() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int granularity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean add(T child) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T get(Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T get(Key[] address, Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Key key) {
		// TODO Auto-generated method stub
		return false;
	}

	

}

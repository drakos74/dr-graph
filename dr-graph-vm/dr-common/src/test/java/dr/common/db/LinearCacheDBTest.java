package dr.common.db;

import dr.common.db.impl.CachedMapDB;

public class LinearCacheDBTest extends BaseDBTest {

	@Override
	protected DB getDB() {
		return new CachedMapDB();
	}

}

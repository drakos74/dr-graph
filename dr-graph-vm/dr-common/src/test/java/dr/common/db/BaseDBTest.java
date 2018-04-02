package dr.common.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class BaseDBTest {

	private DB db;
	
	protected abstract DB getDB();
	
	@Before
	public void before() {
		db = getDB();
	}
	
	@Test
	public void testSimplePut() {
		
		Object k = new Object();
		
		Object v = new Object();
		
		Assert.assertNull(db.put(k, v));
		
		Assert.assertEquals(v, db.get(k));
		
		Assert.assertEquals(1, db.size());
		
	}
	
	@Test
	public void testOverwritePut() {
		
		Object k = new Object();
		
		Object v1 = new Object();
		Object v2 = new Object();

		Assert.assertNull(db.put(k, v1));
		
		Assert.assertEquals(v1, db.put(k,v2));
		
		Assert.assertEquals(v2, db.get(k));
		
		Assert.assertEquals(1, db.size());
		
	}
	
	@Test
	public void testMultiPut() {
		
		Object k1 = new Object();
		Object k2 = new Object();

		Object v1 = new Object();
		Object v2 = new Object();

		Assert.assertNull(db.get(k1));
		Assert.assertNull(db.put(k1, v1));
		
		Assert.assertEquals(1, db.size());
		
		Assert.assertNull(db.get(k2));
		Assert.assertNull(db.put(k2, v2));
				
		Assert.assertEquals(v1, db.get(k1));
		Assert.assertEquals(v2, db.get(k2));
		
		Assert.assertEquals(2, db.size());

	}
	
}

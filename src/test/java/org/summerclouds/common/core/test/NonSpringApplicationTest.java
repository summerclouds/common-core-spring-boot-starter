package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.summerclouds.common.core.M;
import org.summerclouds.common.core.cfg.CfgString;
import org.summerclouds.common.core.concurrent.Lock;
import org.summerclouds.common.core.concurrent.LockManager;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.tool.MSecurity;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MTracing;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclounds.common.internal.TCloseable;
import org.summerclounds.common.junit.TestCase;
import org.summerclounds.common.junit.TestUtil;

public class NonSpringApplicationTest extends TestCase {

	@Test
	public void testLog() {
		// test if logger is working
		Log.getLog(NonSpringApplicationTest.class).i("Hello");
	}
	
	@Test
	public void testTracing() {
		try (IScope scope = MTracing.enter("test")) {
			
		}
	}

	@Test
	public void testSecurity() {
		MSecurity.getCurrent();
	}

	@Test
	public void testValue() throws MException {
		if (!MSpring.isStarted())
		{
			CfgString value = new CfgString( "aaa", "");
			assertEquals("", value.value());
		}
		// changing env is not working with 'mvn install'
		if (!MSpring.isStarted())
			try (TCloseable env = TestUtil.withEnvironment("app.aaa", "bbb")) {
				CfgString value = new CfgString( "aaa", "fallback");
				assertEquals("bbb", value.value());
			}
		else
			log().i("Skip value test with env");
		if (!MSpring.isStarted())
			{
				String rnd = "xxx" + Math.random();
				System.setProperty("app.aaa", rnd);

				CfgString value = new CfgString( "aaa", "fallback");
				assertEquals(rnd, value.value());
			}
		else
			log().i("Skip value test with sys prop");

		if (!MSpring.isStarted())
		{
			String rnd = "xxx" + Math.random();
			System.setProperty("app.bbb.ccc.rnd", rnd);
			System.setProperty("app.bbb.ccc.rnd2", rnd);

			INode bbb = MSpring.getValueNode("bbb", null);
			assertNotNull(bbb);
			
			INode ccc = bbb.getObject("ccc");
			String r = ccc.getString("rnd");
			assertEquals(rnd, r);
			String r2 = ccc.getString("rnd2");
			assertEquals(rnd, r2);
		}
	else
		log().i("Skip value test with sys prop");
		
		
	}

	@Test
	public void testLockManager() {
		LockManager manager = M.l(LockManager.class);
		Lock lock = manager.getLock("test");
		lock.lock();
		lock.unlock();
	}
	
}

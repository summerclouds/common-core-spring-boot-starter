package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.summerclouds.common.core.M;
import org.summerclouds.common.core.cfg.CfgString;
import org.summerclouds.common.core.concurrent.Lock;
import org.summerclouds.common.core.concurrent.LockManager;
import org.summerclouds.common.core.log.Log;
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
	public void testValue() {
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
				if (rnd.equals(System.getProperty("app.aaa"))) {
					CfgString value = new CfgString( "aaa", "fallback");
					assertEquals(rnd, value.value());
				}
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

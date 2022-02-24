package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.summerclouds.common.core.cfg.CfgString;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.tool.MSecurity;
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
		try (TCloseable env = TestUtil.withEnvironment("app.aaa", "bbb")) {
			CfgString value = new CfgString( "aaa", "");
			assertEquals("bbb", value.value());
		}
	}

}

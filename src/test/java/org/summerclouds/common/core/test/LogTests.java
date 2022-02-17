package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.summerclouds.common.core.log.Log;
import org.summerclounds.common.junit.TestCase;

@SpringBootTest
class LogTests extends TestCase  {

	@Test
	void testLogRegistry() {
		Log log1 = Log.getLog(LogTests.class);
		Log log2 = Log.getLog(LogTests.class);
		
		assertEquals(log1, log2);
	}
	
	@Test
	void testLogLevel() {
		Log.getLog(LogTests.class).t("Test trace log");
		Log.getLog(LogTests.class).d("Test debug log");
		Log.getLog(LogTests.class).i("Test info log");
		Log.getLog(LogTests.class).w("Test warn log");
		Log.getLog(LogTests.class).e("Test error log");
		Log.getLog(LogTests.class).f("Test fatal log");
	}

	@Test
	void testLogLevelUpgrade() {
		Log.getLog(LogTests.class).setLocalUpgrade(true);
		assertTrue(Log.getLog(LogTests.class).isLocalUpgrade());
		try {
			Log.getLog(LogTests.class).t("Test trace log");
			Log.getLog(LogTests.class).d("Test debug log");
			Log.getLog(LogTests.class).i("Test info log");
			Log.getLog(LogTests.class).w("Test warn log");
			Log.getLog(LogTests.class).e("Test error log");
			Log.getLog(LogTests.class).f("Test fatal log");
		} finally {
			Log.getLog(LogTests.class).setLocalUpgrade(false);
		}
	}
	
}

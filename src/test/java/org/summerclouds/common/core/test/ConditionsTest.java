package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.summerclouds.common.core.conditions.SConditionalOnProperty;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.junit.TestCase;


@SpringBootTest(classes = {SpringSummerCloudsCoreAutoConfiguration.class},
properties = { 
		"aaa=bbb"
}
)
public class ConditionsTest extends TestCase {

	@Test
	public void testExpressions() {
		{
			boolean b = MSpring.checkConditions(FalseCondition.class);
			assertFalse(b);
		}
		{
			boolean b = MSpring.checkConditions(NoCondition.class);
			assertTrue(b);
		}
		{
			boolean b = MSpring.checkConditions(TrueCondition.class);
			assertTrue(b);
		}
	}
	
	public static class NoCondition {
		
	}
	
	@SConditionalOnProperty(value="aaa",havingValue = "xxx")
	public static class FalseCondition {
		
	}
	
	@SConditionalOnProperty(value="aaa",havingValue = "bbb")
	public static class TrueCondition {
		
	}
}

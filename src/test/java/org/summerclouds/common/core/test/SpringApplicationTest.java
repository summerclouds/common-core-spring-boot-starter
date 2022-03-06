package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.summerclouds.common.core.cfg.CfgString;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.junit.TestCase;

@SpringBootTest(classes = {SpringSummerCloudsCoreAutoConfiguration.class},
				properties = { 
						"bbb.ccc.value1=content1",
						"bbb.ccc.value2=content2",
						"aaa=bbb"
				}
		)

public class SpringApplicationTest extends TestCase {

	@Test
	public void testSpring() throws MException {
		assertTrue( MSpring.isStarted() );
	}
	
	@Test
	public void testCfg() throws MException {
		{
			CfgString value = new CfgString( "aaa", "fallback");
			assertEquals("bbb", value.value());
		}
		{
			CfgString value = new CfgString( "notset", "fallback");
			assertEquals("fallback", value.value());
		}
	}
	
	@Test
	public void testNodeValue() throws MException {
		
		INode bbb = MSpring.getValueNode("bbb", null);
		assertNotNull(bbb);
		
		INode ccc = bbb.getObject("ccc");
		String r = ccc.getString("value1");
		assertEquals("content1", r);
		String r2 = ccc.getString("value2");
		assertEquals("content2", r2);

	}
	
}

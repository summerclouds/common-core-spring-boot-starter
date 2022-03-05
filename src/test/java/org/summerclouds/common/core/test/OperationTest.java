package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.io.OutputStreamProxy;
import org.summerclouds.common.core.node.MNode;
import org.summerclouds.common.core.operation.OperationDescription;
import org.summerclouds.common.core.operation.OperationManager;
import org.summerclouds.common.core.operation.OperationResult;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclounds.common.junit.TestCase;

@SpringBootTest(classes = {SpringSummerCloudsCoreAutoConfiguration.class},
properties = { 
		"org.summerclouds.scan.packages=org.summerclouds.common.core.test.operations",
		"org.summerclouds.operations.enable=true"
}
)
public class OperationTest extends TestCase {

	@Autowired
	OperationManager manager;
	
	@Test
	public void testOperation1() throws Exception {
		assertNotNull(manager);
		
		for (String uri : manager.getOperations()) {
			System.out.println("Operation " + uri);
		}
		
		String uri = "operation://org.summerclouds.common.core.test.operations.operation1:0.0.0";
		
		OperationDescription desc = manager.getDescription(uri);
		assertNotNull(desc);
		
		String obj1 = null;
		String obj2 = null;
		
		MNode config = new MNode();
		{
			OperationResult res = manager.execute(uri, config);
			assertNotNull(res);
			assertTrue(res.isSuccessful());
			obj1 = res.getResultAsNode().getString("object");
			System.out.println(obj1);
		}
		{
			OperationResult res = manager.execute(uri, config);
			assertNotNull(res);
			assertTrue(res.isSuccessful());
			obj2 = res.getResultAsNode().getString("object");
			System.out.println(obj2);
		}
		assertNotEquals(obj1, obj2);

		{
			config.setString("error", "bamm");
			OperationResult res = manager.execute(uri, config);
			assertNotNull(res);
			assertFalse(res.isSuccessful());
		}
		
	}

	@Test
	public void testCmd1() throws Exception {
		assertNotNull(manager);
		
		for (String uri : manager.getOperations()) {
			System.out.println("Operation " + uri);
		}
		
//		String uri = "operation://org.summerclouds.common.core.test.operations.cmd1:0.0.0";
		String uri = "cmd://cmd1";
		
		OperationDescription desc = manager.getDescription(uri);
		System.out.println(desc);
		assertNotNull(desc);

		assertNotNull(desc.getParameterDefinitions().get("0"));
		assertNotNull(desc.getParameterDefinitions().get("opt"));
		
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			OutputStreamProxy osp = new OutputStreamProxy(os);
			osp.setIgnoreClose(true);
			
			MNode config = new MNode();
			config.put(CmdOperation.PARAMETER_OUTPUT_STREAM, osp);
			OperationResult res = manager.execute(uri, config);
			assertNotNull(res);
			assertTrue(res.isSuccessful());
			
			// for async only MThread.waitForWithException(() -> osp.isClosed() , MPeriod.MINUTE_IN_MILLISECONDS * 5);
			String out = new String(os.toByteArray());
			System.out.println("Output:\n" + out);
			assertTrue(out.contains("Finish"));
			assertEquals("Hello", res.getResultAsNode().get(CmdOperation.RESULT_OBJECT));
		}
		
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			OutputStreamProxy osp = new OutputStreamProxy(os);
			osp.setIgnoreClose(true);
			
			MNode config = new MNode();
			config.setString("opt", "error");
			config.put(CmdOperation.PARAMETER_OUTPUT_STREAM, osp);
			OperationResult res = manager.execute(uri, config);
			assertNotNull(res);
			assertFalse(res.isSuccessful());
			assertNull(res.getResultAsNode().get(CmdOperation.RESULT_OBJECT));
			
			// for async only MThread.waitForWithException(() -> osp.isClosed() , MPeriod.MINUTE_IN_MILLISECONDS * 5);
			String out = new String(os.toByteArray());
			System.out.println("Output:\n" + out);
			assertTrue(out.contains("Finish"));
			assertTrue(out.contains("[400,\"test error\"]"));
			assertNull(res.getResultAsNode().get(CmdOperation.RESULT_OBJECT));

		}
	}

	@Test
	public void testCmd2() throws Exception {
		assertNotNull(manager);
		
		String uri = "cmd://cmd2";

		OperationDescription desc = manager.getDescription(uri);
		System.out.println(desc);
		assertNotNull(desc);

		assertNotNull(desc.getParameterDefinitions().get("0"));
		assertNotNull(desc.getParameterDefinitions().get("optString"));

		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStreamProxy osp = new OutputStreamProxy(os);
		osp.setIgnoreClose(true);
		
		MNode config = new MNode();
		config.put(CmdOperation.PARAMETER_OUTPUT_STREAM, osp);
		OperationResult res = manager.execute(uri, config);
		assertNotNull(res);
		assertTrue(res.isSuccessful());
		
		// for async only MThread.waitForWithException(() -> osp.isClosed() , MPeriod.MINUTE_IN_MILLISECONDS * 5);
		String out = new String(os.toByteArray());
		System.out.println("Output:\n" + out);
		assertTrue(out.contains("Finish"));
		assertEquals("Hello", res.getResultAsNode().get(CmdOperation.RESULT_OBJECT));

	}
}

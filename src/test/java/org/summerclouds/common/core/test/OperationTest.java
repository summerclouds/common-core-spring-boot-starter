/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.summerclouds.common.core.operation.util.SuccessfulMap;
import org.summerclouds.common.junit.TestCase;

@SpringBootTest(
        classes = {SpringSummerCloudsCoreAutoConfiguration.class},
        properties = {
            "org.summerclouds.scan.packages=org.summerclouds.common.core.test.operations",
            "org.summerclouds.operations.enabled=true"
        })
public class OperationTest extends TestCase {

    @Autowired OperationManager manager;

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
            System.out.println(res);
            assertNotNull(res);
            assertTrue(res.isSuccessful());

            // for async only MThread.waitForWithException(() -> osp.isClosed() ,
            // MPeriod.MINUTE_IN_MILLISECONDS * 5);
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

            // for async only MThread.waitForWithException(() -> osp.isClosed() ,
            // MPeriod.MINUTE_IN_MILLISECONDS * 5);
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
        assertNotNull(desc.getParameterDefinitions().get("optstring"));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStreamProxy osp = new OutputStreamProxy(os);
        osp.setIgnoreClose(true);

        MNode config = new MNode();
        config.put(CmdOperation.PARAMETER_OUTPUT_STREAM, osp);
        OperationResult res = manager.execute(uri, config);
        assertNotNull(res);
        assertTrue(res.isSuccessful());

        // for async only MThread.waitForWithException(() -> osp.isClosed() ,
        // MPeriod.MINUTE_IN_MILLISECONDS * 5);
        String out = new String(os.toByteArray());
        System.out.println("Output:\n" + out);
        assertTrue(out.contains("\0\nFinish\0"));
        assertEquals("Finish", res.getResultAsNode().get(CmdOperation.RESULT_OBJECT));
    }

    @Test
    public void testSuccessfulMap() {
        SuccessfulMap map = new SuccessfulMap("test", 200, "ok", "a", "b");
        map.put("c", "d");
        map.put("e", new MNode("node"));

        String str = map.toString();
        System.out.println(str);

        assertTrue(str.contains("[SuccessfulMap:[test],[200],[ok],[null],"));
        assertTrue(str.contains("a=b"));
        assertTrue(str.contains("c=d"));
        assertTrue(str.contains("e=node:{}"));
    }
}

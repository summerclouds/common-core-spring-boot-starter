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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.summerclouds.common.core.cfg.CfgString;
import org.summerclouds.common.core.error.ErrorException;
import org.summerclouds.common.core.error.InternalException;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.error.RC;
import org.summerclouds.common.core.error.RC.CAUSE;
import org.summerclouds.common.core.error.RC.Translated;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.junit.TestCase;

@SpringBootTest(
        classes = {SpringSummerCloudsCoreAutoConfiguration.class},
        properties = {"bbb.ccc.value1=content1", "bbb.ccc.value2=content2", "aaa=bbb"})
public class SpringApplicationTest extends TestCase {

    @Test
    public void testSpring() throws MException {
        assertTrue(MSpring.isStarted());
    }

    @Test
    public void testSpringMainPackage() throws MException {
        String main = MSpring.getMainPackages();
        System.out.println(main);
        // test not possible - there is no SpringBootApplication in test environment
        //		assertEquals(getClass().getPackage().getName(), main);
    }

    @Test
    public void testCfg() throws MException {
        {
            CfgString value = new CfgString("aaa", "fallback");
            assertEquals("bbb", value.value());
        }
        {
            CfgString value = new CfgString("notset", "fallback");
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

    @Test
    public void testErrorMessagTranslator() {
        {
            ErrorException msg = new ErrorException("test {1}", "arg1");
            Translated tr = RC.translate(null, msg.getMessage());
            System.out.println(tr);
            assertEquals("test \"arg1\"", tr.getTranslated());
        }
        {
            ErrorException msg = new ErrorException("test {1}");
            Translated tr = RC.translate(null, msg.getMessage());
            System.out.println(tr);
            assertEquals("test {1}", tr.getTranslated());
        }
        {
            ErrorException msg =
                    new ErrorException(
                            CAUSE.APPEND,
                            "test {1}, {2}",
                            "arg1",
                            new InternalException("cause {1}", "nested1"));
            Translated tr = RC.translate(null, msg.getMessage());
            System.out.println(tr);
            assertEquals("test \"arg1\", cause \"nested1\"", tr.getTranslated());
        }
    }
}

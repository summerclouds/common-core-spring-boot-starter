/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.summerclouds.common.core.internal.SpringSummerCloudsAutoConfiguration;
import org.summerclouds.common.core.tool.MThread;
import org.summerclouds.common.core.tool.MThreadDaemon;
import org.summerclounds.common.junit.TestCase;

@SpringBootTest
@ContextConfiguration(classes=SpringSummerCloudsAutoConfiguration.class)
public class MThreadTest extends TestCase {

    protected boolean done;

    @Test
    public void testThread() throws Exception {

        System.out.println(">>> Thread");
        done = false;
        MThread t =
                new MThread(
                                new Runnable() {

                                    @Override
                                    public void run() {
                                        System.out.println("Started");
                                        done = true;
                                    }
                                })
                        .start();
        System.out.println(t);
        assertNotNull(t.getThread());
        MThread.waitForWithException(() -> done, 5000);
    }

    @Test
    public void testThreadDaemon() throws Exception {

        System.out.println(">>> ThreadDaemon");
        done = false;
        MThread t =
                new MThreadDaemon(
                                new Runnable() {

                                    @Override
                                    public void run() {
                                        System.out.println("Started");
                                        done = true;
                                    }
                                })
                        .start();
        System.out.println(t);
        assertNotNull(t.getThread());
        assertEquals(true, t.getThread().isDaemon());
        MThread.waitForWithException(() -> done, 5000);
    }

    @Test
    public void testThreadDirect() throws Exception {

        System.out.println(">>> Thread Direct");
        done = false;
        MThread t =
                new MThread() {
                    @Override
                    public void run() {
                        System.out.println("Started");
                        done = true;
                    }
                }.start();
        System.out.println(t);
        assertNotNull(t.getThread());
        MThread.waitForWithException(() -> done, 5000);
    }

    @Test
    public void testThreadException() throws Exception {

        System.out.println(">>> Thread Exception");
        done = false;
        MThread t =
                new MThread() {
                    @Override
                    public void run() {
                        System.out.println("Started");
                        throw new RuntimeException("Peng");
                    }

                    @Override
                    protected void taskError(Throwable t) {
                        System.out.println(t.getMessage());
                        done = true;
                    }
                }.start();
        System.out.println(t);
        assertNotNull(t.getThread());
        MThread.waitForWithException(() -> done, 5000);
    }

    @BeforeAll
    public static void setUp() throws Exception {
//        MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
    }
}
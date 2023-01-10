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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.summerclouds.common.core.activator.MutableActivator;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.security.DummySecurity;
import org.summerclouds.common.core.security.DummySubject;
import org.summerclouds.common.core.security.DummySubjectEnvironment;
import org.summerclouds.common.core.security.ISecurity;
import org.summerclouds.common.core.security.ISubject;
import org.summerclouds.common.core.security.ISubjectEnvironment;
import org.summerclouds.common.core.tool.MSecurity;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MThread;
import org.summerclouds.common.core.tool.MThreadDaemon;
import org.summerclouds.common.core.tool.MTracing;
import org.summerclouds.common.core.tracing.DummyScope;
import org.summerclouds.common.core.tracing.DummySpan;
import org.summerclouds.common.core.tracing.DummyTracing;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;
import org.summerclouds.common.core.tracing.ITracing;
import org.summerclouds.common.core.util.Value;
import org.summerclouds.common.junit.TestCase;

@SpringBootTest
@ContextConfiguration(classes = SpringSummerCloudsCoreAutoConfiguration.class)
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

    @Test
    public void testThreadControl() {

        try {
            Value<Boolean> run = new Value<Boolean>(true);

            ((MutableActivator) MSpring.getDefaultActivator())
                    .register(
                            ISecurity.class,
                            new DummySecurity() {

                                ThreadLocal<ISubject> users = new ThreadLocal<>();

                                public ISubjectEnvironment asSubject(ISubject user) {
                                    users.set(user);
                                    return new DummySubjectEnvironment(user);
                                }

                                public ISubjectEnvironment asSubject(String username) {
                                    DummySubject user = new DummySubject(username);
                                    users.set(user);
                                    return new DummySubjectEnvironment(user);
                                }

                                @Override
                                public ISubject getCurrent() {
                                    ISubject user = users.get();
                                    return user;
                                }
                            });

            ((MutableActivator) MSpring.getDefaultActivator())
                    .register(
                            ITracing.class,
                            new DummyTracing() {

                                ThreadLocal<ISpan> spans = new ThreadLocal<>();

                                @Override
                                public ISpan current() {
                                    return spans.get();
                                }

                                @Override
                                public IScope enter(ISpan span, String name, Object... keyValue) {
                                    DummySpan s = new DummySpan(span + " " + name);
                                    spans.set(s);
                                    return new DummyScope(s);
                                }
                            });

            final Value<String> innerUser = new Value<>();
            final Value<String> innerSpan = new Value<>();

            try (IScope scope = MTracing.enter(null, "testSpan")) {
                try (ISubjectEnvironment secEnv = MSecurity.asSubject("testSubject")) {

                    MThread.asynchron(
                            new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        innerUser.setValue(MSecurity.getCurrent().toString());
                                        innerSpan.setValue(MTracing.current().toString());
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                    run.setValue(false);
                                }
                            });
                }
            }

            // wait for end
            while (run.getValue()) {
                MThread.sleepForSure(100);
            }

            System.out.println(innerUser);
            System.out.println(innerSpan);
            assertEquals("testSubject", innerUser.getValue());
            assertEquals("null testSpan thread", innerSpan.getValue());

        } finally {
            ((MutableActivator) MSpring.getDefaultActivator()).register(ISecurity.class, null);
            ((MutableActivator) MSpring.getDefaultActivator()).register(ITracing.class, null);
        }
    }

    @BeforeAll
    public static void setUp() throws Exception {
        //        MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
    }
}

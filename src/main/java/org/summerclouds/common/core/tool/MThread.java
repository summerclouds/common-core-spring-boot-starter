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
package org.summerclouds.common.core.tool;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.summerclouds.common.core.cfg.BeanRefMap;
import org.summerclouds.common.core.error.InterruptedRuntimeException;
import org.summerclouds.common.core.error.TimeoutRuntimeException;
import org.summerclouds.common.core.lang.Checker;
import org.summerclouds.common.core.lang.ICloseable;
import org.summerclouds.common.core.lang.IThreadControl;
import org.summerclouds.common.core.lang.Named;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.log.PlainLog;
import org.summerclouds.common.core.log.ThreadConsoleLogAppender;
import org.summerclouds.common.core.security.ISubject;
import org.summerclouds.common.core.security.ISubjectEnvironment;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;
import org.summerclouds.common.core.util.ThreadPool;
import org.summerclouds.common.core.util.Value;
import org.summerclouds.common.core.util.ValueProvider;

/**
 * @author hummel
 *     <p>To change the template for this generated type comment go to
 *     Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MThread extends MLog implements Runnable {

    protected static Log log = Log.getLog(MThread.class);
    private static ThreadLocal<Map<String, Object>> threadContext = new ThreadLocal<>();
    private static BeanRefMap<IThreadControl> threadControllers =
            new BeanRefMap<>(IThreadControl.class);

    protected Runnable task = this;
    protected String name = null;
    protected Thread thread = null;

    private int priority = -1;

    public MThread() {}

    public MThread(String _name) {
        name = _name;
    }

    public MThread(Runnable _task) {
        task = _task;
    }

    public MThread(Runnable _task, String _name) {
        task = _task;
        name = _name;
    }

    protected Runnable getTask() {
        return task;
    }

    @Override
    public void run() {}

    public MThread start() {
        synchronized (this) {
            if (thread != null) throw new IllegalThreadStateException();
            Container container = new Container();
            thread = new Thread(getGroup(), container);
            initThread(thread);
            thread.start();
        }
        return this;
    }

    private static ThreadGroup group = new ThreadGroup("MThread");

    protected ThreadGroup getGroup() {
        return group;
    }

    protected void initThread(Thread thread) {
        if (priority != -1) thread.setPriority(priority);

        if (name == null) {
            if (task == null) name = "null";
            else if (task instanceof Named) name = "MThread " + ((Named) task).getName();
            else name = "MThread " + MSystem.getCanonicalClassName(task.getClass());
        }

        thread.setName(name);
    }

    private class Container implements Runnable {

        //        private final long parentThreadId = Thread.currentThread().getId();
        //        private final ISpan span =  MTracing.current();
        //        private final ISubject subject = MSecurity.getCurrent();
        private final HashMap<String, Object> context = new HashMap<>();

        public Container() {
            prepareNewThread(context);
        }

        @Override
        public void run() {
            initNewThread(context);
            //            try (ISubjectEnvironment env = MSecurity.asSubject(subject)) {
            //                try (IScope scope =
            //                        MTracing.enter(
            //                                        span,
            //                                        "Thread: " + name,
            //                                        "thread",
            //                                        "" + thread.getId(),
            //                                        "parent",
            //                                        "" + parentThreadId)) {
            //                    log().t("###: NEW THREAD", parentThreadId, thread.getId());
            try {
                if (task != null) task.run();
            } catch (Exception t) {
                taskError(t);
            }
            releaseThread(context);
            //                    log.t("###: LEAVE THREAD", thread.getId());
        }
        //            }
        //        }
    }

    public void setName(String _name) {
        this.name = _name;
        if (thread != null) thread.setName(_name);
    }

    public String getName() {
        return name;
    }

    public void setPriority(int _p) {
        this.priority = _p;
        if (thread != null) thread.setPriority(_p);
    }

    public int getPriority() {
        return priority;
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (thread == null) return;
        thread.stop();
    }

    public void interupt() {
        if (thread == null) return;
        thread.interrupt();
    }

    @Override
    public String toString() {
        if (thread != null)
            return MSystem.toString(
                    "MThread", name, thread.getId(), thread.getPriority(), thread.getState());
        else return MSystem.toString("MThread", name);
    }

    /**
     * Sleeps _millisec milliseconds. On Interruption it will throw an RuntimeInterruptedException
     *
     * @param _millisec
     */
    public static void sleep(long _millisec) {
        try {
            Thread.sleep(_millisec);
        } catch (InterruptedException e) {
            throw new InterruptedRuntimeException(e);
        }
    }

    /**
     * Sleeps _millisec milliseconds. On Interruption it will throw an InterruptedException. If
     * thread is already interrupted, it will throw the exception directly.
     *
     * <p>This can be used in loops if a interrupt should be able to stop the loop.
     *
     * @param _millisec
     * @throws InterruptedException on interrupt
     */
    public static void sleepInLoop(long _millisec) throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();
        Thread.sleep(_millisec);
    }

    /**
     * Sleeps _millisec milliseconds. On Interruption it will print a debug stack trace but not
     * break. It will leave the Thread.interrupted state to false see
     * https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html
     *
     * @param _millisec
     * @return true if the thread was interrupted in the sleep time
     */
    public static boolean sleepForSure(long _millisec) {
        boolean interrupted = false;
        while (true) {
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(_millisec);
                return interrupted;
            } catch (InterruptedException e) {
                interrupted = true;
                try {
                    Thread.sleep(1); // clear interrupted state
                } catch (InterruptedException e1) {
                }
                log.d(e);
                long done = System.currentTimeMillis() - start;
                _millisec = _millisec - done;
                if (_millisec <= 0) return interrupted;
            }
        }
    }

    protected void taskError(Throwable t) {
        log().e(name, t);
    }

    public static void asynchron(Runnable task) {
        new MThread(task).start();
    }

    /**
     * Try every 200ms to get the value. If the provider throws an error or return null the try will
     * be repeated. If the time out is reached a TimeoutRuntimeException will be thrown.
     *
     * @param provider
     * @param timeout
     * @param nullAllowed
     * @return The requested value
     */
    public static <T> T getWithTimeout(
            final ValueProvider<T> provider, long timeout, boolean nullAllowed) {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                T val = provider.getValue();
                if (nullAllowed || val != null) return val;
            } catch (Exception t) {
            }
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
    }

    /**
     * Like getWithTimeout but executed in a separate task, this means unblocking.
     *
     * @param provider
     * @param timeout
     * @param nullAllowed
     * @return The requested value
     */
    public static <T> T getAsynchronWithTimeout(
            final ValueProvider<T> provider, long timeout, boolean nullAllowed) {
        long start = System.currentTimeMillis();
        final Value<T> value = new Value<>();
        final HashMap<String, Object> context = new HashMap<>();
        prepareNewThread(context);
        ThreadPool t =
                new ThreadPool(
                        new Runnable() {

                            @Override
                            public void run() {
                                initNewThread(context);
                                while (true) {
                                    try {
                                        T val = provider.getValue();
                                        if (nullAllowed || val != null) {
                                            value.setValue(val);
                                            break;
                                        }
                                    } catch (Exception t) {
                                    }
                                    if (System.currentTimeMillis() - start > timeout)
                                        throw new TimeoutRuntimeException();
                                    sleep(200);
                                }
                                releaseThread(context);
                            }
                        });
        t.start();
        while (t.isAlive()) {
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
        return value.getValue();
    }

    /**
     * Calls the provider once and will return the result. The provider is called in a separate
     * thread to
     *
     * @param provider
     * @param timeout
     * @return The requested value
     * @throws Exception
     */
    public static <T> T getAsynchronWithTimeout(final ValueProvider<T> provider, long timeout)
            throws Exception {
        long start = System.currentTimeMillis();
        final Value<T> value = new Value<>();
        final Value<Throwable> error = new Value<>();

        final HashMap<String, Object> context = new HashMap<>();
        prepareNewThread(context);

        ThreadPool t =
                new ThreadPool(
                        new Runnable() {

                            @Override
                            public void run() {
                                initNewThread(context);
                                try {
                                    value.setValue(provider.getValue());
                                } catch (Exception t) {
                                    error.setValue(t);
                                }
                                releaseThread(context);
                            }
                        });
        t.start();
        while (t.isAlive()) {
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
        if (error.getValue() != null) {
            if (error.getValue() instanceof RuntimeException)
                throw (RuntimeException) error.getValue();
            if (error.getValue() instanceof Exception) throw (Exception) error.getValue();
            throw new Exception(error.getValue());
        }
        return value.getValue();
    }

    /**
     * Wait for the checker to return true or throw an TimeoutRuntimeException on timeout. A
     * exception in the checker will be ignored.
     *
     * @param checker
     * @param timeout
     */
    public static void waitFor(final Checker checker, long timeout) {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                if (checker.check()) return;
            } catch (Exception t) {
            }
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
    }

    /**
     * Wait for the checker to return true or throw an TimeoutRuntimeException on timeout.
     *
     * @param checker
     * @param timeout
     * @throws Exception Thrown if checker throws an exception
     */
    public static void waitForWithException(final Checker checker, long timeout) throws Exception {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                if (checker.check()) return;
            } catch (Exception t) {
                throw t;
            }
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
    }

    public Thread getThread() {
        return thread;
    }

    /**
     * Check if the thread was interrupted an throws the InterruptedException exception.
     *
     * @throws InterruptedException Throw if the thread was interrupted in the meantime.
     */
    public static void checkInterruptedException() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();
    }

    public static void run(Runnable task) {
        final HashMap<String, Object> context = new HashMap<>();
        prepareNewThread(context);
        new Thread(
                        new Runnable() {

                            @Override
                            public void run() {
                                initNewThread(context);
                                try {
                                    task.run();
                                } catch (Exception t) {
                                    t.printStackTrace();
                                }
                                releaseThread(context);
                            }
                        })
                .start();
    }

    public static void run(Consumer<Thread> consumer) {
        final HashMap<String, Object> context = new HashMap<>();
        prepareNewThread(context);
        new Thread(
                        new Runnable() {

                            @Override
                            public void run() {
                                initNewThread(context);
                                try {
                                    consumer.accept(Thread.currentThread());
                                } catch (Exception t) {
                                    t.printStackTrace();
                                }
                                releaseThread(context);
                            }
                        })
                .start();
    }

    /** Execute to cleanup a used thread for a new task. */
    public static void cleanup() {
        try {
            MSecurity.get().subjectCleanup();
            MTracing.get().cleanup();
            ThreadConsoleLogAppender.cleanup();
            MSystem.ioCleanup();
            Map<String, IThreadControl> map = threadControllers.beans();
            if (map != null) {
                for (IThreadControl control : map.values()) control.cleanup();
            }
        } catch (Exception t) {
            PlainLog.f(t);
        }
    }

    /*
     * Will be execute in the old thread environment and fill the context with information for the new
     * created thread.
     */
    public static void prepareNewThread(Map<String, Object> context) {
        try {
            context.put("parentThreadId", Thread.currentThread().getId());
            ISpan span = MTracing.current();
            if (span != null) context.put("span", span);
            ISubject subject = MSecurity.getCurrent();
            if (subject != null) context.put("subject", subject);
            OutputStream logAppenderStream = ThreadConsoleLogAppender.current();
            if (logAppenderStream != null) context.put("logAppenderStream", logAppenderStream);
            if (MSystem.isOutOverlay() || MSystem.isErrOverlay() || MSystem.isInOverlay()) {
                context.put("out", MSystem.getOutOverlay());
                context.put("err", MSystem.getErrOverlay());
                context.put("in", MSystem.getInOverlay());
            }
            Map<String, IThreadControl> map = threadControllers.beans();
            if (map != null) {
                for (IThreadControl control : map.values()) control.prepareNewThread(context);
            }
        } catch (Exception t) {
            PlainLog.f(t);
        }
    }

    /**
     * Will be executed in the new thread with context filled in prepareNewThread
     *
     * @param context
     */
    public static void initNewThread(Map<String, Object> context) {
        threadContext.set(context);
        try {
            log.d(
                    "new thread {1} created from {2}",
                    Thread.currentThread().getId(), context.get("parentThreadId"));
            ISubject subject = (ISubject) context.get("subject");
            if (subject != null) {
                try {
                    ISubjectEnvironment env = MSecurity.asSubject(subject);
                    context.put("subjectEnv", env);
                } catch (Exception t) {
                    PlainLog.e(t);
                }
            }
            ISpan span = (ISpan) context.get("span");
            if (span != null) {
                try {
                    IScope scope =
                            MTracing.enter(
                                    span,
                                    "thread",
                                    "" + Thread.currentThread().getId(),
                                    "parent",
                                    "" + context.get("parentThreadId"));
                    context.put("spanScope", scope);
                } catch (Exception t) {
                    PlainLog.e(t);
                }
            }
            OutputStream logAppenderStream = (OutputStream) context.get("logAppenderStream");
            if (logAppenderStream != null) {
                ThreadConsoleLogAppender.sendTo(logAppenderStream);
            }
            OutputStream out = (OutputStream) context.get("out");
            OutputStream err = (OutputStream) context.get("err");
            InputStream in = (InputStream) context.get("in");

            if (out != null && err != null && in != null) {
                ICloseable io = MSystem.useIO(out, err, in);
                context.put("io", io);
            }

            Map<String, IThreadControl> map = threadControllers.beans();
            if (map != null) {
                for (IThreadControl control : map.values()) control.initNewThread(context);
            }
        } catch (Exception t) {
            PlainLog.f(t);
        }
    }

    /**
     * Will be executed before leaving.
     *
     * @param context
     */
    public static void releaseThread(Map<String, Object> context) {
        try {
            Map<String, IThreadControl> map = threadControllers.beans();
            if (map != null) {
                for (IThreadControl control : map.values()) control.releaseThread(context);
            }
        } catch (Exception t) {
            PlainLog.f(t);
        }
        try {
            IScope scope = (IScope) context.get("spanScope");
            if (scope != null) scope.close();
        } catch (Exception t) {
            PlainLog.f(t);
        }
    }

    public static Map<String, Object> context() {
        return threadContext.get();
    }
}

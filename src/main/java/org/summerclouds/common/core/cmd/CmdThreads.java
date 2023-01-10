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
package org.summerclouds.common.core.cmd;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.summerclouds.common.core.console.ConsoleTable;
import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdArgument;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.operation.cmd.CmdOption;
import org.summerclouds.common.core.tool.MCast;

@OperationComponent(path = "core.thread", description = "Print thread information")
public class CmdThreads extends CmdOperation {

    @CmdArgument(
            index = 0,
            name = "thread",
            required = false,
            description = "Thread Id",
            multiValued = false)
    String threadId;

    @CmdArgument(
            index = 1,
            name = "action",
            required = false,
            description = "suspend, resume, stop, destroy, interrupt, priority <int>",
            multiValued = false)
    String action;

    @CmdArgument(
            index = 2,
            name = "arguments",
            required = false,
            description = "arguments",
            multiValued = true)
    String arguments[];

    @CmdOption(
            name = "-s",
            description = "print also stack traces",
            required = false,
            multiValued = false)
    boolean stackAlso;

    @CmdOption(name = "-i", description = "order by id", required = false, multiValued = false)
    boolean orderId;

    @CmdOption(name = "-n", description = "order by name", required = false, multiValued = false)
    boolean orderName;

    @CmdOption(name = "-g", description = "order by group", required = false, multiValued = false)
    boolean orderGroup;

    @CmdOption(name = "-f", description = "Filter Threads", required = false, multiValued = false)
    String[] filter;

    ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();

    @SuppressWarnings("deprecation")
    @Override
    public String executeCmd() throws Exception {

        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();

        List<Thread> threadList = new LinkedList<>(traces.keySet());

        if (filter != null) {
            for (String f : filter) {
                threadList.removeIf(i -> !matchFilter(f, i));
            }
        }

        if (orderId) {
            Collections.sort(
                    threadList,
                    new Comparator<Thread>() {

                        @Override
                        public int compare(Thread o1, Thread o2) {
                            return Long.compare(o1.getId(), o2.getId());
                        }
                    });
        }

        if (orderName) {
            Collections.sort(
                    threadList,
                    new Comparator<Thread>() {

                        @Override
                        public int compare(Thread o1, Thread o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
        }

        if (orderGroup) {
            Collections.sort(
                    threadList,
                    new Comparator<Thread>() {

                        @Override
                        public int compare(Thread o1, Thread o2) {
                            return o1.getThreadGroup()
                                    .getName()
                                    .compareTo(o2.getThreadGroup().getName());
                        }
                    });
        }

        ConsoleTable table = createTable();
        table.setHeaderValues("Id", "Name", "Group", "Status", "Prio", "Alive", "Daemon");

        if (threadId == null) {

            for (Thread thread : threadList) {
                printThread(thread, table);
                if (stackAlso) {
                    StackTraceElement[] stack = traces.get(thread);
                    printStack(stack, table);
                }
            }

        } else if (action != null) {
            for (Thread thread : traces.keySet()) {
                if (String.valueOf(thread.getId()).equals(threadId)
                        || thread.getName().equals(threadId)
                        || threadId.equals("current") && thread == Thread.currentThread()) {
                    switch (action) {
                        case "suspend":
                            {
                                System.out.println(
                                        "SUSPEND " + thread.getId() + " " + thread.getName());
                                thread.suspend();
                            }
                            break;
                        case "resume":
                            {
                                System.out.println(
                                        "RESUME " + thread.getId() + " " + thread.getName());
                                thread.resume();
                            }
                            break;
                        case "stop":
                            {
                                System.out.println(
                                        "STOP " + thread.getId() + " " + thread.getName());
                                thread.stop();
                            }
                            break;
                        case "interrupt":
                            {
                                System.out.println(
                                        "INTERRUPT " + thread.getId() + " " + thread.getName());
                                thread.interrupt();
                            }
                            break;
                        case "kill":
                            {
                                String message = arguments[0];
                                Throwable exception = new Throwable(message);
                                if (arguments.length > 1) {
                                    exception =
                                            (Throwable)
                                                    Class.forName(arguments[1])
                                                            .getConstructor(String.class)
                                                            .newInstance(message);
                                }
                                System.out.println(
                                        "STOP "
                                                + thread.getId()
                                                + " "
                                                + thread.getName()
                                                + " by "
                                                + exception);
                                thread.stop();
                            }
                            break;
                        case "destroy":
                            {
                                System.out.println(
                                        "DESTROY " + thread.getId() + " " + thread.getName());
                                // thread.destroy();
                            }
                            break;
                        case "priority":
                            {
                                System.out.println(
                                        "PRIORITY "
                                                + thread.getId()
                                                + " "
                                                + thread.getName()
                                                + " = "
                                                + arguments[0]);
                                thread.setPriority(MCast.toint(arguments[0], thread.getPriority()));
                            }
                            break;
                    }
                }
            }
        } else {
            for (Thread thread : traces.keySet()) {
                if (String.valueOf(thread.getId()).equals(threadId)
                        || thread.getName().equals(threadId)
                        || threadId.equals("current") && thread == Thread.currentThread()) {
                    printThread(thread, table);
                    StackTraceElement[] stack = traces.get(thread);
                    printStack(stack, table);

                    ThreadInfo info = tmxb.getThreadInfo(thread.getId());
                    table.addRowValues(
                            "LockOwnerName", "" + info.getLockOwnerName(), "", "", "", "");
                    table.addRowValues("LockOwnerId", "" + info.getLockOwnerId(), "", "", "", "");
                    table.addRowValues("LockName", "" + info.getLockName(), "", "", "", "");
                    table.addRowValues("BlockedCount", "" + info.getBlockedCount(), "", "", "", "");
                    table.addRowValues("BlockedTime", "" + info.getBlockedTime(), "", "", "", "");
                    table.addRowValues("WaitedCount", "" + info.getWaitedCount(), "", "", "", "");
                    table.addRowValues("WaitedTime", "" + info.getWaitedTime(), "", "", "", "");
                    table.addRowValues("LockInfo", "" + info.getLockInfo(), "", "", "", "");
                    for (MonitorInfo lock : info.getLockedMonitors()) {
                        table.addRowValues(
                                "Monitor",
                                "" + lock.getClassName(),
                                "" + lock.getIdentityHashCode(),
                                "" + lock.getLockedStackDepth(),
                                "",
                                "");
                        table.addRowValues(
                                "", "  at " + lock.getLockedStackFrame(), "", "", "", "");
                    }
                    for (LockInfo lock : info.getLockedSynchronizers()) {
                        table.addRowValues(
                                "Synchronizers",
                                "" + lock.getClassName(),
                                "" + lock.getIdentityHashCode(),
                                "",
                                "",
                                "");
                    }
                }
            }
        }
        table.print();
        return null;
    }

    private boolean matchFilter(String filter, Thread t) {

        String[] parts = filter.split(":", 2);
        if (parts.length == 1) {
            // TODO
            return true;
        }

        String k = parts[0].toLowerCase().trim();
        String v = parts[1];

        switch (k) {
            case "state":
                return t.getState().name().equalsIgnoreCase(v);
            case "name":
                return t.getName().contains(v);
            case "stack":
                return MCast.toString(t.getStackTrace()).contains(v);
            case "group":
                return t.getThreadGroup().getName().contains(v);
        }
        return false;
    }

    private void printStack(StackTraceElement[] stack, ConsoleTable table) {
        for (StackTraceElement line : stack) table.addRowValues("", "  at " + line, "", "", "", "");
    }

    private void printThread(Thread thread, ConsoleTable table) {
        ThreadGroup g = thread.getThreadGroup();

        table.addRowValues(
                thread.getId(),
                thread.getName(),
                g == null ? "" : g.getName(),
                thread.getState(),
                thread.getPriority(),
                thread.isAlive(),
                thread.isDaemon());
    }
}

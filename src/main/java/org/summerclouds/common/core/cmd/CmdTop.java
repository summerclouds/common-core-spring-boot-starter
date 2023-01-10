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

import java.lang.Thread.State;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.summerclouds.common.core.console.ConsoleTable;
import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.operation.cmd.CmdOption;
import org.summerclouds.common.core.tool.MPeriod;
import org.summerclouds.common.core.tool.MSystem;
import org.summerclouds.common.core.tool.MSystem.TopThreadInfo;

@OperationComponent(path = "core.top", description = "Print thread status information")
public class CmdTop extends CmdOperation {

    @CmdOption(
            name = "-s",
            description = "print also stack traces extract",
            required = false,
            multiValued = false)
    boolean stackAlso;

    @CmdOption(
            name = "--orderid",
            description = "order by id",
            required = false,
            multiValued = false)
    boolean orderId;

    @CmdOption(name = "-n", description = "order by name", required = false, multiValued = false)
    boolean orderName;

    @CmdOption(name = "-c", description = "order by cputime", required = false, multiValued = false)
    boolean orderCpuTime = true;

    @CmdOption(name = "-r", description = "Running only", required = false, multiValued = false)
    boolean running;

    @CmdOption(name = "-i", description = "Interval", required = false, multiValued = false)
    long sleep = 2000;

    @CmdOption(name = "-t", description = "order by time", required = false, multiValued = false)
    boolean orderTime = false;

    @CmdOption(
            name = "-a",
            description = "print absolut values",
            required = false,
            multiValued = false)
    boolean absolut = false;

    @CmdOption(
            name = "-x",
            description = "Simple output format, set lines to output",
            required = false,
            multiValued = false)
    int raw = 0;

    DecimalFormat twoDForm = new DecimalFormat("#.00");

    @Override
    public String executeCmd() throws Exception {

        List<TopThreadInfo> threads = MSystem.threadTop(sleep);
        if (running)
            threads.removeIf(
                    i -> {
                        return i.getThread().getState() != State.RUNNABLE;
                    });

        if (orderId) {
            Collections.sort(
                    threads,
                    new Comparator<TopThreadInfo>() {

                        @Override
                        public int compare(TopThreadInfo o1, TopThreadInfo o2) {
                            return Long.compare(o1.getThread().getId(), o2.getThread().getId());
                        }
                    });
        } else if (orderName) {
            Collections.sort(
                    threads,
                    new Comparator<TopThreadInfo>() {

                        @Override
                        public int compare(TopThreadInfo o1, TopThreadInfo o2) {
                            return o1.getThread().getName().compareTo(o2.getThread().getName());
                        }
                    });
        } else if (orderTime) {
            Collections.sort(
                    threads,
                    new Comparator<TopThreadInfo>() {

                        @Override
                        public int compare(TopThreadInfo o1, TopThreadInfo o2) {
                            return Long.compare(o2.getCpuTotal(), o1.getCpuTotal());
                        }
                    });
        } else if (orderCpuTime) {
            Collections.sort(
                    threads,
                    new Comparator<TopThreadInfo>() {

                        @Override
                        public int compare(TopThreadInfo o1, TopThreadInfo o2) {
                            return Long.compare(o2.getCpuTime(), o1.getCpuTime());
                        }
                    });
        }

        if (raw > 0) {
            System.out.println("Id;Name;Status;Cpu;User;Time;Stacktrace");
            int cnt = 0;
            for (TopThreadInfo t : threads) {
                if (absolut) {
                    System.out.println(
                            t.getThread().getId()
                                    + ";"
                                    + t.getThread().getName()
                                    + ";"
                                    + t.getThread().getState()
                                    + ";"
                                    + t.getCpuTime()
                                    + ";"
                                    + t.getUserTime()
                                    + ";"
                                    + t.getCpuTotal()
                                    + ";"
                                    + (stackAlso ? toString(t.getStacktrace()) : ""));
                } else {
                    System.out.println(
                            t.getThread().getId()
                                    + ";"
                                    + t.getThread().getName()
                                    + ";"
                                    + t.getThread().getState()
                                    + ";"
                                    + twoDForm.format(t.getCpuPercentage())
                                    + ";"
                                    + twoDForm.format(t.getUserPercentage())
                                    + ";"
                                    + MPeriod.getIntervalAsStringSec(t.getCpuTotal() / 1000000)
                                    + ";"
                                    + (stackAlso ? toString(t.getStacktrace()) : ""));
                }
                cnt++;
                if (cnt >= raw) break;
            }
            System.out.println();
        } else {
            ConsoleTable table = createTable();
            int height = getConsole().getHeight();
            int width = getConsole().getWidth();
            table.setHeaderValues("Id", "Name", "Status", "Cpu", "User", "Time", "Stacktrace");
            table.getHeader().get(1).weight = 1;
            if (stackAlso) table.getHeader().get(6).weight = 1;
            table.setMaxTableWidth(width);
            //			table.setMaxColSize(Math.max( (width - 60) / 2, 30) );
            for (TopThreadInfo t : threads) {
                if (table.size() + 3 >= height) break;
                if (absolut) {
                    table.addRowValues(
                            t.getThread().getId(),
                            t.getThread().getName(),
                            t.getThread().getState(),
                            t.getCpuTime(),
                            t.getUserTime(),
                            t.getCpuTotal(),
                            stackAlso ? toString(t.getStacktrace()) : "");
                } else {
                    table.addRowValues(
                            t.getThread().getId(),
                            t.getThread().getName(),
                            t.getThread().getState(),
                            twoDForm.format(t.getCpuPercentage()),
                            twoDForm.format(t.getUserPercentage()),
                            MPeriod.getIntervalAsStringSec(t.getCpuTotal() / 1000000),
                            stackAlso ? toString(t.getStacktrace()) : "");
                }
            }

            table.print();
        }

        return null;
    }

    public static String toString(StackTraceElement[] trace) {
        StringBuilder sb = new StringBuilder();
        if (trace == null) return sb.toString();

        for (int i = 0; i < trace.length; i++) {
            String cName = trace[i].getClassName();
            if (!cName.startsWith("sun.")
                    && trace[i].getLineNumber() >= 0
                    && !cName.startsWith("java.util.")
                    && !cName.startsWith("java.net.")
                    && !cName.startsWith("com.google.common.")
                    && !cName.startsWith("org.apache.common.")
                    && !cName.startsWith("java.lang.")) {
                if (sb.length() > 0) sb.append("/");
                sb.append(cName).append('.').append(trace[i].getMethodName());
            }
        }
        String str = sb.toString();
        return str;
    }
}

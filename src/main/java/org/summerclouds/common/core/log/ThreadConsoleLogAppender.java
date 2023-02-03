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
package org.summerclouds.common.core.log;

import java.io.OutputStream;

import org.springframework.context.SmartLifecycle;
import org.summerclouds.common.core.lang.ICloseable;
import org.summerclouds.common.core.tool.MDate;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class ThreadConsoleLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent>
        implements SmartLifecycle {

    private static ThreadLocal<OutputStream> stream = new ThreadLocal<>();

    public static void cleanup() {
        stream.set(null);
    }

    public static ICloseable sendTo(OutputStream os) {
        final OutputStream current = stream.get();
        stream.set(os);
        return new ICloseable() {

            @Override
            public void close() {
                stream.set(current);
            }
        };
    }

    @Override
    public boolean isRunning() {
        return isStarted();
    }

    @Override
    protected void append(ILoggingEvent event) {
        OutputStream os = stream.get();
        if (os != null) {
            try {
                String msg =
                        MDate.toIsoDateTime(event.getTimeStamp())
                                + " "
                                + event.getLevel()
                                + " "
                                + event.getLoggerName()
                                + " "
                                + event.getFormattedMessage();
                os.write(msg.getBytes());
            } catch (Exception t) {
            }
        }
    }

    public static OutputStream current() {
        return stream.get();
    }
}

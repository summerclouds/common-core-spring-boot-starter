package org.summerclouds.common.core.log;

import java.io.OutputStream;

import org.springframework.context.SmartLifecycle;
import org.summerclouds.common.core.lang.ICloseable;
import org.summerclouds.common.core.tool.MDate;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class ThreadConsoleLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements SmartLifecycle {

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
				String msg = MDate.toIsoDateTime(event.getTimeStamp()) + " " + event.getLevel() + " " + event.getLoggerName() + " " + event.getFormattedMessage();
				os.write(msg.getBytes());
			} catch (Throwable t) {}
		}
	}

	public static OutputStream current() {
		return stream.get();
	}

}

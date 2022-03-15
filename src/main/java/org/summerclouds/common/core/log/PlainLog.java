package org.summerclouds.common.core.log;

import org.summerclouds.common.core.console.Console;
import org.summerclouds.common.core.console.DefaultConsoleFactory;
import org.summerclouds.common.core.console.Console.COLOR;
import org.summerclouds.common.core.error.RC;
import org.summerclouds.common.core.error.RC.CAUSE;
import org.summerclouds.common.core.log.Log.LEVEL;
import org.summerclouds.common.core.tool.MDate;
import org.summerclouds.common.core.tool.MString;

public class PlainLog {

    private static int FIX_NAME_LENGTH = 30;

    private static COLOR COLOR_TIME = COLOR.BRIGHT_BLACK;

    private static COLOR COLOR_TRACE = COLOR.BRIGHT_BLACK;
    private static COLOR COLOR_DEBUG = COLOR.BLUE;
    private static COLOR COLOR_INFO = COLOR.GREEN;
    private static COLOR COLOR_WARN = COLOR.BRIGHT_RED;
    private static COLOR COLOR_ERROR = COLOR.RED;
    private static COLOR COLOR_FATAL = COLOR.MAGENTA;

    private static COLOR COLOR_NAME = COLOR.BRIGHT_BLACK;
    private static COLOR COLOR_MESSAGE = COLOR.BRIGHT_WHITE;

    private static boolean traces = true;
    
	private static boolean printTime = true;

	private static LEVEL level = LEVEL.DEBUG;

	private static Console out = new DefaultConsoleFactory().create(null);
	private static LogFacade facade = new ConsoleLog();

	private static int maxMsgSize = 3000;
	
	public static void log(LEVEL level, String msg, Object... param) {
		
		switch (level) {
		case DEBUG:
			if (!facade.isDebugEnabled())
				return;
			break;
		case ERROR:
			if (!facade.isErrorEnabled())
				return;
			break;
		case FATAL:
			if (!facade.isFatalEnabled())
				return;
			break;
		case INFO:
			if (!facade.isInfoEnabled())
				return;
			break;
		case TRACE:
			if (!facade.isTraceEnabled())
				return;
			break;
		case WARN:
			if (!facade.isWarnEnabled())
				return;
			break;
		default:
			return;
		}

		msg = "[" + Thread.currentThread().getId() + "]" + (msg != null ? msg : "");
		msg = RC.toMessage(-1, CAUSE.ENCAPSULATE, msg, param, maxMsgSize );
		Throwable error = RC.findCause(CAUSE.ENCAPSULATE, param);

		switch (level) {
		case DEBUG:
			facade.debug(msg, error);
			break;
		case ERROR:
			facade.error(msg, error);
			break;
		case FATAL:
			facade.fatal(msg, error);
			break;
		case INFO:
			facade.info(msg, error);
			break;
		case TRACE:
			facade.trace(msg, error);
			break;
		case WARN:
			facade.warn(msg, error);
			break;
		default:
			break;
		}


	}

	/**
	 * Log a message in trace, it will automatically append the objects if trace is
	 * enabled. Can Also add a trace. This is the local trace method. The trace will
	 * only written if the local trace is switched on.
	 *
	 * @param msg
	 */
	public static void t(String msg, Object... param) {
		log(LEVEL.TRACE, msg, param);
	}

	public static void t(Throwable t) {
		log(LEVEL.TRACE, t.toString(), t);
	}

	/**
	 * Log a message in debug, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public static void d(String msg, Object... param) {
		log(LEVEL.DEBUG, msg, param);
	}

	public static void d(Throwable t) {
		log(LEVEL.DEBUG, t.toString(), t);
	}

	/**
	 * Log a message in info, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public static void i(String msg, Object... param) {
		log(LEVEL.INFO, msg, param);
	}

	public static void i(Throwable t) {
		log(LEVEL.INFO, t.toString(), t);
	}

	/**
	 * Log a message in warn, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public static void w(String msg, Object... param) {
		log(LEVEL.WARN, msg, param);
	}

	public static void w(Throwable t) {
		log(LEVEL.WARN, t.toString(), t);
	}

	/**
	 * Log a message in error, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public static void e(String msg, Object... param) {
		log(LEVEL.ERROR, msg, param);
	}

	public static void e(Throwable t) {
		log(LEVEL.ERROR, t.toString(), t);
	}

	/**
	 * Log a message in info, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public static void f(String msg, Object... param) {
		log(LEVEL.FATAL, msg, param);
	}

	public static void f(Throwable t) {
		log(LEVEL.FATAL, t.toString(), t);
	}

    static class ConsoleLog extends LogFacade {

        public ConsoleLog() {
            super("");
        }

        @Override
        public void debug(Object message) {
            if (!isDebugEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_DEBUG, null);
            out.print("DEBUG ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void debug(Object message, Throwable t) {
            if (!isDebugEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_DEBUG, null);
            out.print("DEBUG ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void error(Object message) {
            if (!isErrorEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_ERROR, null);
            out.print("ERROR ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void error(Object message, Throwable t) {
            if (!isErrorEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_ERROR, null);
            out.print("ERROR ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void fatal(Object message) {
            if (!isFatalEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_FATAL, null);
            out.print("FATAL ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void fatal(Object message, Throwable t) {
            if (!isFatalEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_FATAL, null);
            out.print("FATAL ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void info(Object message) {
            if (!isInfoEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_INFO, null);
            out.print("INFO  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void info(Object message, Throwable t) {
            if (!isInfoEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_INFO, null);
            out.print("INFO  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public boolean isDebugEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.DEBUG.ordinal();
        }

        @Override
        public boolean isErrorEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.ERROR.ordinal();
        }

        @Override
        public boolean isFatalEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.FATAL.ordinal();
        }

        @Override
        public boolean isInfoEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.INFO.ordinal();
        }

        @Override
        public boolean isTraceEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.TRACE.ordinal();
        }

        @Override
        public boolean isWarnEnabled() {
            return getLevel().ordinal() <= Log.LEVEL.WARN.ordinal();
        }

        @Override
        public void trace(Object message) {
            if (!isTraceEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_TRACE, null);
            out.print("TRACE ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void trace(Object message, Throwable t) {
            if (!isTraceEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_TRACE, null);
            out.print("TRACE ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void warn(Object message) {
            if (!isWarnEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_WARN, null);
            out.print("WARN  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (message != null && message instanceof Throwable && traces)
                ((Throwable) message).printStackTrace(out);
            out.cleanup();
        }

        @Override
        public void warn(Object message, Throwable t) {
            if (!isWarnEnabled()) return;
            out.setColor(COLOR_TIME, null);
            out.print(printTime());
            out.setColor(COLOR_WARN, null);
            out.print("WARN  ");
            out.setColor(COLOR_NAME, null);
            out.print(getFixName());
            out.print(" ");
            out.setColor(COLOR_MESSAGE, null);
            out.println(message);
            if (t != null && traces) t.printStackTrace(out);
            out.cleanup();
        }

        private String getFixName() {

            String n = getName();
            if (n.length() > FIX_NAME_LENGTH) n = n.substring(0, FIX_NAME_LENGTH);
            else if (n.length() < FIX_NAME_LENGTH)
                n = n + MString.rep(' ', FIX_NAME_LENGTH - n.length());
            return n;
        }

        @Override
        public void doInitialize(LogFactory logFactory) {}

        @Override
        public void close() {}
        
		@Override
		public String getName() {
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			return MString.afterLastIndex(stack[6].getClassName(), ".");
		}

	    public String printTime() {
	        if (printTime) {
	            return MDate.toIso8601(System.currentTimeMillis()) + " ";
	        }
	        return "";
	    }

	    public Log.LEVEL getLevel() {
	        return level;
	    }

    }
}

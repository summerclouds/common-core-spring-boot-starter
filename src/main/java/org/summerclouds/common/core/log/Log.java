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
package org.summerclouds.common.core.log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.tool.MString;
import org.summerclouds.common.core.tool.MSystem;

/**
 * Got the interface from apache-commons-logging. I need to switch because its not working in
 * eclipse plugins correctly.
 *
 * @author mikehummel
 */
public class Log {

    public enum LEVEL {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    };

    protected static ParameterMapper parameterMapper;
    private static int maxMsgSize = 0;
    private static boolean verbose = false;
    private static boolean stacktraceTrace;
    @Autowired(required = false)
    private static LogFactory factory = new SLF4JFactory();
    
    private static WeakHashMap<String, Log> registry = new WeakHashMap<>();
    
    private LogFacade facade;
    protected String name;
    private List<String> maxMsgSizeExceptions;
	private boolean localUpgrade;

    private Log(String name) {
    	this.name = name;
    	facade = factory.create(name);
    }

    // -------------------------------------------------------- Logging Methods

    /**
     * Log a message in trace, it will automatically append the objects if trace is enabled. Can
     * Also add a trace. This is the local trace method. The trace will only written if the local
     * trace is switched on.
     *
     * @param msg
     */
    public void t(Object... msg) {
        log(LEVEL.TRACE, msg);
    }

    public void log(LEVEL level, Object... msg) {
        if (facade == null) return;

        // level mapping
        if (verbose) {
            if (level == LEVEL.DEBUG) level = LEVEL.INFO;
        }

        switch (level) {
            case DEBUG:
                if (!facade.isDebugEnabled()) return;
                break;
            case ERROR:
                if (!facade.isErrorEnabled()) return;
                break;
            case FATAL:
                if (!facade.isFatalEnabled()) return;
                break;
            case INFO:
                if (!facade.isInfoEnabled()) return;
                break;
            case TRACE:
                if (!facade.isTraceEnabled()) return;
                break;
            case WARN:
                if (!facade.isWarnEnabled()) return;
                break;
            default:
                return;
        }

        if (parameterMapper != null) msg = parameterMapper.map(this, msg);

        StringBuilder sb = new StringBuilder();
        prepare(sb);
        Throwable error = MString.serialize(sb, msg, maxMsgSize, maxMsgSizeExceptions);

        switch (level) {
            case DEBUG:
                facade.debug(sb.toString(), error);
                break;
            case ERROR:
                facade.error(sb.toString(), error);
                break;
            case FATAL:
                facade.fatal(sb.toString(), error);
                break;
            case INFO:
                facade.info(sb.toString(), error);
                break;
            case TRACE:
                facade.trace(sb.toString(), error);
                break;
            case WARN:
                facade.warn(sb.toString(), error);
                break;
            default:
                break;
        }

        if (stacktraceTrace) {
            String stacktrace =
                    MCast.toString("stacktracetrace", Thread.currentThread().getStackTrace());
            facade.debug(stacktrace);
        }
    }


    // toos from MDate
    protected static String toIsoDateTime(Date _in) {
        Calendar c = Calendar.getInstance();
        c.setTime(_in);
        return toIsoDateTime(c);
    }

    protected static String toIsoDateTime(Calendar _in) {
        return _in.get(Calendar.YEAR)
                + "-"
                + toDigits(_in.get(Calendar.MONTH) + 1, 2)
                + "-"
                + toDigits(_in.get(Calendar.DAY_OF_MONTH), 2)
                + " "
                + toDigits(_in.get(Calendar.HOUR_OF_DAY), 2)
                + ":"
                + toDigits(_in.get(Calendar.MINUTE), 2)
                + ":"
                + toDigits(_in.get(Calendar.SECOND), 2);
    }

    protected static String toDigits(int _in, int _digits) {
        StringBuilder out = new StringBuilder().append(Integer.toString(_in));
        while (out.length() < _digits) out.insert(0, '0');
        return out.toString();
    }

    //	/**
    //     * Log a message in trace, it will automatically append the objects if trace is enabled.
    // Can Also add a trace.
    //     */
    //    public void tt(Object ... msg) {
    //    	if (!isTraceEnabled()) return;
    //    	StringBuilder sb = new StringBuilder();
    //    	prepare(sb);
    //    	Throwable error = null;
    ////    	int cnt=0;
    //    	for (Object o : msg) {
    //			error = serialize(sb,o, error);
    ////    		cnt++;
    //    	}
    //    	trace(sb.toString(),error);
    //    }

    /**
     * Log a message in debug, it will automatically append the objects if debug is enabled. Can
     * Also add a trace.
     *
     * @param msg
     */
    public void d(Object... msg) {
        log(LEVEL.DEBUG, msg);
    }

    /**
     * Log a message in info, it will automatically append the objects if debug is enabled. Can Also
     * add a trace.
     *
     * @param msg
     */
    public void i(Object... msg) {
        log(LEVEL.INFO, msg);
    }

    /**
     * Log a message in warn, it will automatically append the objects if debug is enabled. Can Also
     * add a trace.
     *
     * @param msg
     */
    public void w(Object... msg) {
        log(LEVEL.WARN, msg);
    }

    /**
     * Log a message in error, it will automatically append the objects if debug is enabled. Can
     * Also add a trace.
     *
     * @param msg
     */
    public void e(Object... msg) {
        log(LEVEL.ERROR, msg);
    }

    /**
     * Log a message in info, it will automatically append the objects if debug is enabled. Can Also
     * add a trace.
     *
     * @param msg
     */
    public void f(Object... msg) {
        log(LEVEL.FATAL, msg);
    }

    protected void prepare(StringBuilder sb) {
        sb.append('[').append(Thread.currentThread().getId()).append(']');
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MSystem.toString(this, getName());
    }

    public static synchronized Log getLog(Object owner) {

    	try {
	    	String name = MSystem.getOwnerName(owner);
	    	Log log = registry.get(name);
	    	if (log == null) {
	    		log = new Log(name);
	    		registry.put(name, log);
	    	}
	    	return log;
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    public ParameterMapper getParameterMapper() {
        return parameterMapper;
    }

    /**
     * Return if the given level is enabled. This function also uses the levelMapper to find the
     * return value. Instead of the is...Enabled().
     *
     * @param level
     * @return true if level is enabled
     */
    public boolean isLevelEnabled(LEVEL level) {
        if (facade == null) return false;

        if (localUpgrade) level = LEVEL.INFO;

        switch (level) {
            case DEBUG:
                return facade.isDebugEnabled();
            case ERROR:
                return facade.isErrorEnabled();
            case FATAL:
                return facade.isFatalEnabled();
            case INFO:
                return facade.isInfoEnabled();
            case TRACE:
                return facade.isTraceEnabled();
            case WARN:
                return facade.isWarnEnabled();
            default:
                return false;
        }
    }

    public void close() {
        if (facade == null) return;
        //		unregister();
        facade.close();
        facade = null;
    }

    public static boolean isStacktraceTrace() {
        return stacktraceTrace;
    }

    public static boolean isVerbose() {
        return verbose;
    }

    public static int getMaxMsgSize() {
        return maxMsgSize;
    }

	public boolean isLocalUpgrade() {
		return localUpgrade;
	}

	public void setLocalUpgrade(boolean localUpgrade) {
		this.localUpgrade = localUpgrade;
	}

	public static LogFactory getFactory() {
		return factory;
	}

	public static void setFactory(LogFactory factory) {
		Log.factory = factory;
	}

}

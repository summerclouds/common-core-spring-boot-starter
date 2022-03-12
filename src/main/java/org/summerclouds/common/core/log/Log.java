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

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.cfg.CfgBoolean;
import org.summerclouds.common.core.cfg.CfgInt;
import org.summerclouds.common.core.error.RC;
import org.summerclouds.common.core.error.RC.CAUSE;
import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.tool.MSystem;
import org.summerclouds.common.core.tool.MTracing;
import org.summerclouds.common.core.util.SoftHashMap;

/**
 * Got the interface from apache-commons-logging. I need to switch because its
 * not working in eclipse plugins correctly.
 *
 * @author mikehummel
 */
public class Log {

	public enum LEVEL {
		TRACE, DEBUG, INFO, WARN, ERROR, FATAL
	};

	protected ParameterMapper parameterMapper = new DefaultParameterMapper();
	private static CfgInt maxMsgSize = new CfgInt(Log.class,"maxMsgSize", 10000);
	private static CfgBoolean logTraceId = new CfgBoolean(Log.class, "logTraceId", false);
	private static boolean stacktraceTrace;

	private static SoftHashMap<String, Log> registry = new SoftHashMap<>();

	protected LogFacade facade;
	protected String name;
	private boolean localUpgrade;
	private static boolean globalUpgrade;

	protected Log(String name) {
		this.name = name;

	}

	// -------------------------------------------------------- Logging Methods

	public void log(LEVEL level, String msg, Object... param) {
		if (facade == null) {
			try {
				facade = M.l(LogFactory.class).create(name);
			} catch (Throwable t) {
				System.out.println("*** " + t);
				t.printStackTrace();
			}
		}
		// level mapping
		if (globalUpgrade || localUpgrade) {
			if (level == LEVEL.DEBUG || level == LEVEL.TRACE)
				level = LEVEL.INFO;
		}

		if (facade == null) {
			if (level != LEVEL.TRACE) {
				msg = RC.toMessage(-1, CAUSE.ENCAPSULATE, msg, param, maxMsgSize.value());
				Throwable error = RC.findCause(CAUSE.ENCAPSULATE, param);
				System.out.println(level + " " + msg);
				if (error != null)
					error.printStackTrace();
			}
			return;
		}

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

		if (parameterMapper != null)
			param = parameterMapper.map(this, param);

		msg = RC.toMessage(-1, CAUSE.ENCAPSULATE, msg, param, maxMsgSize.value());
		msg = postMsg(msg);
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

		if (stacktraceTrace) {
			String stacktrace = MCast.toString("stacktracetrace", Thread.currentThread().getStackTrace());
			facade.debug(stacktrace);
		}
	}

	private String postMsg(String msg) {
		if (logTraceId.value())
			return MTracing.getCurrentId() + "#" + msg;
		return msg;
	}

	/**
	 * Log a message in trace, it will automatically append the objects if trace is
	 * enabled. Can Also add a trace. This is the local trace method. The trace will
	 * only written if the local trace is switched on.
	 *
	 * @param msg
	 */
	public void t(String msg, Object... param) {
		log(LEVEL.TRACE, msg, param);
	}

	public void t(Throwable t) {
		log(LEVEL.TRACE, t.toString(), t);
	}

	/**
	 * Log a message in debug, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public void d(String msg, Object... param) {
		log(LEVEL.DEBUG, msg, param);
	}

	public void d(Throwable t) {
		log(LEVEL.DEBUG, t.toString(), t);
	}

	/**
	 * Log a message in info, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public void i(String msg, Object... param) {
		log(LEVEL.INFO, msg, param);
	}

	public void i(Throwable t) {
		log(LEVEL.INFO, t.toString(), t);
	}

	/**
	 * Log a message in warn, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public void w(String msg, Object... param) {
		log(LEVEL.WARN, msg, param);
	}

	public void w(Throwable t) {
		log(LEVEL.WARN, t.toString(), t);
	}

	/**
	 * Log a message in error, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public void e(String msg, Object... param) {
		log(LEVEL.ERROR, msg, param);
	}

	public void e(Throwable t) {
		log(LEVEL.ERROR, t.toString(), t);
	}

	/**
	 * Log a message in info, it will automatically append the objects if debug is
	 * enabled. Can Also add a trace.
	 *
	 * @param msg
	 */
	public void f(String msg, Object... param) {
		log(LEVEL.FATAL, msg, param);
	}

	public void f(Throwable t) {
		log(LEVEL.FATAL, t.toString(), t);
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
	 * Return if the given level is enabled. This function also uses the levelMapper
	 * to find the return value. Instead of the is...Enabled().
	 *
	 * @param level
	 * @return true if level is enabled
	 */
	public boolean isLevelEnabled(LEVEL level) {
		if (facade == null)
			return false;

		if (localUpgrade)
			level = LEVEL.INFO;

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
		if (facade == null)
			return;
		// unregister();
		facade.close();
		facade = null;
	}

	public static boolean isStacktraceTrace() {
		return stacktraceTrace;
	}

	public static int getMaxMsgSize() {
		return maxMsgSize.value();
	}

	public boolean isLocalUpgrade() {
		return localUpgrade;
	}

	public void setLocalUpgrade(boolean localUpgrade) {
		this.localUpgrade = localUpgrade;
	}

	public static boolean isGlobalUpgrade() {
		return globalUpgrade;
	}

	public static void setGlobalUpgrade(boolean globalUpgrade) {
		Log.globalUpgrade = globalUpgrade;
	}

}

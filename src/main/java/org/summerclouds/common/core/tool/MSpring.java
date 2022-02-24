package org.summerclouds.common.core.tool;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.summerclouds.common.core.activator.Activator;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.internal.ContextListener;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.log.PlainLog;
import org.summerclouds.common.core.node.INode;

public class MSpring {

	public enum STATUS {
		BOOT,
		STARTED,
		STOPPED, 
		CLOSED
	};

	private static ApplicationContext context;
	
	private static Environment environment;

	private static STATUS status = STATUS.BOOT;

	private static Activator activator;

	@SuppressWarnings("unchecked")
	public static <T> T lookup(Class<T> class1) {
		if (class1 == null) throw new NullPointerException();
		if (context == null) {
			return (T) getDefaultActivator().getObject(class1.getCanonicalName());
		}
		try {
			return context.getBean(class1);
		} catch (BeansException e) {
			PlainLog.e("Bean error {1}", class1.getCanonicalName(),e);
			return (T) getDefaultActivator().getObject(class1.getCanonicalName());
//			throw new MRuntimeException(RC.STATUS.ERROR, class1.getCanonicalName(), e);
		}
	}

	public static void setContext(ApplicationContext appContext) {
		MSystem.acceptCaller(SpringSummerCloudsCoreAutoConfiguration.class);
		context = appContext;
	}

	public static void setEnvironment(Environment env) {
		MSystem.acceptCaller(SpringSummerCloudsCoreAutoConfiguration.class);
		environment = env;
	}

	public static String getValue(String key) {
		if (environment == null) return null;
		return environment.getProperty(key,String.class);
	}

	public static Integer getValueInt(String key) {
		if (environment == null) return null;
		return environment.getProperty(key,Integer.class);
	}
	
	public static Long getValueLong(String key) {
		if (environment == null) return null;
		return environment.getProperty(key,Long.class);
	}

	public static INode getValueNode(String name, INode def) {
		String content = getValue(name);
		if (content == null) return def;
		try {
			return INode.readNodeFromString(content);
		} catch (MException e) {
			Log.getLog(MSpring.class).w("can't read node key {1}", name,e);
		}
		return def;
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
		return context.getBeansOfType(clazz);
	}
	
	//XXX Prefix ?
	public static String toValueKey(Object owner, String key) {
		if (owner == null) return key;
		return MSystem.getOwnerName(owner).replace('.', '_') + '.' + key;
	}
	
	public static boolean isStarted() {
		return context != null && environment != null && status == STATUS.STARTED;
	}

	public static void setStatus(STATUS status) {
		MSystem.acceptCaller(ContextListener.class);
		PlainLog.i("Status changed", status);
		MSpring.status = status;
	}
	
	public static STATUS getStatus() {
		return status;
	}

	public static ClassLoader getDefaultClassLoader() {
		return MSpring.class.getClassLoader();
	}

	public static Activator getDefaultActivator() {
		if (activator == null) activator = new Activator();
		return activator;
	}
	
}

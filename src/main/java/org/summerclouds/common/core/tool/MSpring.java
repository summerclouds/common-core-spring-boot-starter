package org.summerclouds.common.core.tool;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.summerclouds.common.core.error.MRuntimeException;
import org.summerclouds.common.core.error.RC;
import org.summerclouds.common.core.internal.ContextListener;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.log.PlainLog;
import org.summerclouds.common.core.util.Activator;

public class MSpring {

	public enum STATUS {
		BOOT,
		STARTED,
		STOPPED, 
		CLOSED
	};

	private static ApplicationContext context;
	
	private static WeakHashMap<String, Object> defaultBeans = new WeakHashMap<>();

	private static Environment environment;

	private static STATUS status = STATUS.BOOT;

	private static Activator activator;

	public static <T> T lookup(Class<T> class1) {
		if (class1 == null) throw new NullPointerException();
		try {
			return context.getBean(class1);
		} catch (BeansException e) {
			PlainLog.e("Bean error {1}", class1.getCanonicalName());
			e.printStackTrace();
			throw new MRuntimeException(RC.STATUS.ERROR, class1.getCanonicalName(), e);
		}
	}

	public static <T,D> T lookup(Class<T> class1, Class<D> def) {
		if (class1 == null) throw new NullPointerException();
		if (context == null) return localDefaultBean(class1, def);
		try {
			return context.getBean(class1);
		} catch (BeansException e) {
			if (def == null) return null;
			return localDefaultBean(class1, def);
		}
	}

	@SuppressWarnings("unchecked")
	private static synchronized <T,D> T localDefaultBean(Class<T> class1, Class<D> def) {
		Object obj = defaultBeans.get(def.getCanonicalName());
		if (obj == null) {
			PlainLog.e("Create bean {1} fallback {2}", class1.getCanonicalName(), def.getCanonicalName());
			try {
				obj = def.getConstructor().newInstance();
				defaultBeans.put(def.getCanonicalName(), obj);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				throw new MRuntimeException(RC.STATUS.ERROR, def.getCanonicalName(), e);
			}
		}
		try {
			return (T) obj;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new MRuntimeException(RC.STATUS.ERROR, def.getCanonicalName(), e);
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

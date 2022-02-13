package org.summerclouds.common.core.tool;

import java.lang.reflect.InvocationTargetException;
import java.util.WeakHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.summerclouds.common.core.error.MRuntimeException;
import org.summerclouds.common.core.error.RC;

public class MSpring {

	private static ApplicationContext context;
	
	private static WeakHashMap<String, Object> defaultBeans = new WeakHashMap<>();

	private static Environment environment;

	public static <T> T lookup(Class<T> class1) {
		if (class1 == null) throw new NullPointerException();
		try {
			return context.getBean(class1);
		} catch (BeansException e) {
			System.out.println("*** Bean error " + class1.getCanonicalName());
			e.printStackTrace();
			throw new MRuntimeException(RC.STATUS.ERROR, class1.getCanonicalName(), e);
		}
	}

	public static <T,D> T lookup(Class<T> class1, Class<D> def) {
		if (class1 == null) throw new NullPointerException();
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
			System.out.println("*** Create bean " + class1.getCanonicalName() + " fallback " + def.getCanonicalName());
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
		context = appContext;
	}

	public static void setEnvironment(Environment env) {
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
	
}

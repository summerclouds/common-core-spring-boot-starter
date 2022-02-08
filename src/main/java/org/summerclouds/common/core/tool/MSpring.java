package org.summerclouds.common.core.tool;

import java.lang.reflect.InvocationTargetException;
import java.util.WeakHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.summerclouds.common.core.error.MRuntimeException;
import org.summerclouds.common.core.error.RC;

public class MSpring {

	public static ApplicationContext context;
	
	private static WeakHashMap<String, Object> defaultBeans = new WeakHashMap<>();

	public static <T> T lookup(Class<T> class1) {
		if (class1 == null) throw new NullPointerException();
		try {
			return context.getBean(class1);
		} catch (BeansException e) {
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
			return localDefaultBean(def);
		}
	}

	@SuppressWarnings("unchecked")
	private static synchronized <T,D> T localDefaultBean(Class<D> def) {
		Object obj = defaultBeans.get(def.getCanonicalName());
		if (obj == null) {
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

}

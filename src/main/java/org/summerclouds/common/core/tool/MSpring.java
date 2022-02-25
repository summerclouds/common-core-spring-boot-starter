package org.summerclouds.common.core.tool;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.summerclouds.common.core.activator.Activator;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.internal.ContextListener;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.log.PlainLog;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.node.IProperties;
import org.summerclouds.common.core.node.MNode;
import org.summerclouds.common.core.node.MProperties;
import org.summerclouds.common.core.node.PropertiesSubset;

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

	private static Set<String> noLookup = Collections.synchronizedSet(new HashSet<>());

	@SuppressWarnings("unchecked")
	public static <T> T lookup(Class<T> class1) {
		if (class1 == null) throw new NullPointerException();
		if (context == null || noLookup.contains(class1.getCanonicalName())) {
			return (T) getDefaultActivator().getObject(class1.getCanonicalName());
		}
		try {
			return context.getBean(class1);
		} catch (BeansException e) {
			PlainLog.e("Bean error {1}", class1.getCanonicalName(),e.toString());
			if (PlainValues.getBoolean("sping.lookup.debug", false))
				e.printStackTrace();
			if (isStarted())
				noLookup.add(class1.getCanonicalName());
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
		// option 1: value as json
		String content = getValue(name);
		if (content != null) {
			try {
				return INode.readNodeFromString(content);
			} catch (MException e) {
				Log.getLog(MSpring.class).w("can't read node key {1}", name,e);
			}
		}
		// option 2: as multiple properties
		IProperties values = new PropertiesSubset(getAllValues(), name + ".");
		if (values.size() > 0) {
			MNode root = new MNode();
			for (Entry<String, Object> item : values.entrySet()) {
				String key = item.getKey();
				String p = MString.beforeLastIndex(key, '.'); // path
				String n = MString.afterLastIndex(key, '.'); // name
				INode target = INode.findOrCreateNode(root, p.replace('.', '/'));
				target.put(n, item.getValue());
			}
			return root;
		}
		return def;
	}

	@SuppressWarnings("rawtypes")
	public static IProperties getAllValues() {
		MProperties props = new MProperties();
		if (environment != null) {
			MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
			StreamSupport.stream(propSrcs.spliterator(), false)
			        .filter(ps -> ps instanceof EnumerablePropertySource)
			        .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
			        .flatMap(Arrays::<String>stream)
			        .forEach(propName -> props.setProperty(propName, environment.getProperty(propName)));
		} else {
			StreamSupport.stream(System.getenv().entrySet().spliterator(), false)
						.filter(pair -> pair.getKey().startsWith("app."))
						.forEach(pair -> props.put(pair.getKey().substring(4), pair.getValue()));
			StreamSupport.stream(System.getProperties().entrySet().spliterator(), false)
			.filter(pair -> String.valueOf(pair.getKey()).startsWith("app."))
			.forEach(pair -> props.put(String.valueOf(pair.getKey()).substring(4), pair.getValue()));
		}
		return props;
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
		if (status == STATUS.CLOSED)
			noLookup.clear();
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

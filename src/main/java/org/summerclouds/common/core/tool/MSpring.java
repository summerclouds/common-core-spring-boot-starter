package org.summerclouds.common.core.tool;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.summerclouds.common.core.M;
import org.summerclouds.common.core.activator.Activator;
import org.summerclouds.common.core.activator.MutableActivator;
import org.summerclouds.common.core.cfg.CfgString;
import org.summerclouds.common.core.condition.SummerCondition;
import org.summerclouds.common.core.condition.SummerConditional;
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

	private static String mainPackage;

	private static Object springBootApplication;

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
		if (context == null) return null;
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
		if (activator == null) activator = new MutableActivator();
		return activator;
	}
	
	public static <T> List<Class<? extends T>> findAnnotatedClasses(Class<? extends Annotation> annotationType, boolean checkConditions) {
		return findAnnotatedClasses(null, annotationType, checkConditions);
	}
	
	public static <T> List<Class<? extends T>> findAnnotatedClasses(String scanPackageList, Class<? extends Annotation> annotationType, boolean checkConditions) {
		
		if (MString.isEmpty(scanPackageList)) {
			scanPackageList = new CfgString("org.summerclouds.scan.packages", null).value();
		}
		if (MString.isEmpty(scanPackageList)) {
			scanPackageList = "org.summerclouds"; // TODO find application main and add package
		}
		
		ArrayList<Class<? extends T>> entities = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider provider = createComponentScanner(annotationType);
        for (String scanPackage : scanPackageList.split(","))
	        for (BeanDefinition beanDef : provider.findCandidateComponents(scanPackage)) {
	        	try {
	        		@SuppressWarnings("unchecked")
					Class<? extends T> cl = (Class<? extends T>) MSystem.getClass(beanDef.getBeanClassName());
	        		if (!checkConditions || checkConditions(cl))
	        			entities.add(cl);
	        	} catch (Throwable t) {
	        		PlainLog.e("can't load xdb entity {1}",beanDef.getBeanClassName());
	        	}
	        }
        return entities;
    }
 
    private static ClassPathScanningCandidateComponentProvider createComponentScanner(Class<? extends Annotation> annotationType) {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        return provider;
    }

	public static ApplicationContext getContext() {
		return context;
	}

	public static Environment getEnvironment() {
		return environment;
	}
	
	public static boolean checkConditions(Class<?> clazz) {

		try {
			ArrayList<Object[]> conditions = new ArrayList<>();
			// find
			for (Annotation anno : clazz.getAnnotations()) {
				SummerConditional cc = anno.annotationType().getAnnotation(SummerConditional.class);
				if (cc != null) {
					Class<? extends SummerCondition>[] cclazzes = cc.value();
					for (Class<? extends SummerCondition> cclazz : cclazzes) {
						conditions.add(new Object[] { cclazz, anno });
					}
				}
			}
			// test
			for (Object[] obj : conditions) {
				Class<?> c = (Class<?>)obj[0];
				try {
					SummerCondition condition = (SummerCondition)MSystem.createObject(c);
					if (!condition.matches((Annotation)obj[1], clazz))
						return false;
				} catch (Throwable t) {
					PlainLog.e("checkConditions failed for {1} in {2}",clazz, c, t);
					return false;
				}
			}
			return true;
		} catch (Throwable t) {
			PlainLog.e("checkConditions failed for {1}",clazz,t);
		}
		return false;
	}
	
	public static String getMainPackage() {
		if (mainPackage == null) {
			Map<String, Object> candidates = context.getBeansWithAnnotation(SpringBootApplication.class);
			mainPackage = candidates.isEmpty() ? M.class.getPackageName() : candidates.values().toArray()[0].getClass().getPackageName();
		}
		return mainPackage;
	}
	
	public static Object getSpringBootApplication() {
		if (springBootApplication == null) {
			Map<String, Object> candidates = context.getBeansWithAnnotation(SpringBootApplication.class);
			springBootApplication = candidates.isEmpty() ? null : candidates.values().toArray()[0];
		}
		return springBootApplication;
	}
}

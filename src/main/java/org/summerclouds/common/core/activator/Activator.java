package org.summerclouds.common.core.activator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.summerclouds.common.core.log.PlainLog;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MSystem;

public class Activator extends ClassLoader {

	protected Map<String,Object> registry = Collections.synchronizedMap(new HashMap<>());
	
	public Activator(ClassLoader parent) {
		super(parent);
	}
	
	public Activator() {
		this(MSpring.getDefaultClassLoader());
	}

	public Object getObject(String name) {
		Object obj = registry.get(name);
		if (obj != null) return obj;
		obj = createObject(name);
		if (obj == null) return null;
		registry.put(name, obj);
		return obj;
	}

	public Object createObject(String obj) {
		try {
			Class<?> clazz = MSystem.getClass(obj);
			
	        DefaultImplementation defaultImplementation =
	                clazz.getAnnotation(DefaultImplementation.class);
	        if (defaultImplementation != null) {
	            clazz = defaultImplementation.value();
	        }

	        Object out = null;

	        DefaultFactory factoryClazz = clazz.getAnnotation(DefaultFactory.class);
	        if (factoryClazz != null) {
	            ObjectFactory inst = (ObjectFactory) getObject(factoryClazz.value().getCanonicalName());
	            if (inst != null) {
	                out = inst.create(clazz);
	            }
	        }
	        
	        if (out == null)
	        	out = MSystem.createObject(this, clazz);
	        
			return out;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			PlainLog.w("failed to create object for class {1}", obj ,e);
		}
		return null;
	}
	
}

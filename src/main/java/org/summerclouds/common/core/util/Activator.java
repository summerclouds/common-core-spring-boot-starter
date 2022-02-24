package org.summerclouds.common.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MSystem;

public class Activator extends ClassLoader {

	private Map<String,Object> registry = new HashMap<>();
	
	public Activator(ClassLoader parent) {
		super(parent);
	}
	
	public Activator() {
		this(MSpring.getDefaultClassLoader());
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
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
			return MSystem.createObject(this, obj);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Class<?> findClass(String name) throws ClassNotFoundException {
		return loadClass(name);
	}

}

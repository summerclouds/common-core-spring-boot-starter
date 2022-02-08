package org.summerclouds.common.core.util;

public class Activator extends ClassLoader {

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
	}

	public Object getObject(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object createObject(String obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public Class<?> findClass(String type) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public Class<?> getClazz(String className) {
		// TODO Auto-generated method stub
		return null;
	}

}

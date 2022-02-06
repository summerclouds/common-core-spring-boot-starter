package org.summerclouds.common.core.util;

public class MActivator {

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
	}

}

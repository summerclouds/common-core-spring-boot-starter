package org.summerclouds.common.core.activator;

public class MutableActivator extends Activator {

	
	public void register(String name, Object obj) {
		registry.put(name, obj);
	}
	
	public boolean isRegistered(String name) {
		return registry.containsKey(name);
	}
	
}

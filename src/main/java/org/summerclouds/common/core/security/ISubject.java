package org.summerclouds.common.core.security;

public interface ISubject {

	String getName();
	
	Object getPrincipal();

	boolean hasRole(String rolename);

	boolean hasPermission(String ace);

	boolean hasPermission(String object, String action, String instance);

	boolean hasPermission(ISubject subject, Class<?> object, String action, String instance);
	
}

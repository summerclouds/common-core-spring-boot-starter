package org.summerclouds.common.core.security;

public class DummySubject implements ISubject {

	private String name;

	public DummySubject() {
		this("");
	}

	public DummySubject(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Object getPrincipal() {
		return name;
	}

	@Override
	public boolean hasRole(String rolename) {
		return true;
	}

	@Override
	public boolean hasPermission(String ace) {
		return true;
	}

	@Override
	public boolean hasPermission(String object, String action, String instance) {
		return true;
	}

	@Override
	public boolean hasPermission(ISubject subject, Class<?> object, String action, String instance) {
		return true;
	}

}

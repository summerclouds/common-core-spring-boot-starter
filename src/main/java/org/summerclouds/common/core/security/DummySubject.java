package org.summerclouds.common.core.security;

public class DummySubject implements ISubject {

	@Override
	public String getName() {
		return "?";
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Object getPrincipal() {
		return null;
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

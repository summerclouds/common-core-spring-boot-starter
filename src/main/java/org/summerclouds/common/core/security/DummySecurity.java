package org.summerclouds.common.core.security;

import javax.security.auth.Subject;

import org.summerclouds.common.core.error.AuthorizationException;

public class DummySecurity implements ISecurity {

	public static final ISubject SUBJECT = new DummySubject();
	public static final ISubjectEnvironment SUBJECT_ENVIRONMENT = new DummySubjectEnvironment();

	@Override
	public ISubject getSubject() {
		return SUBJECT;
	}

	@Override
	public ISubjectEnvironment asSubjectWithoutTracing(ISubject subject) {
		return SUBJECT_ENVIRONMENT;
	}

	@Override
	public void subjectCleanup() {
	}

	@Override
	public String toString(Subject subject) {
		return "?";
	}

	@Override
	public boolean isAnnotated(Class<?> clazz) {
		return false;
	}

	@Override
	public void checkPermission(Class<?> clazz) throws AuthorizationException {
		
	}

}

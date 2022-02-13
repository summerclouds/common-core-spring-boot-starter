package org.summerclouds.common.core.security;

import javax.security.auth.Subject;

public interface ISecurity {

	ISubject getSubject();

	ISubjectEnvironment asSubjectWithoutTracing(ISubject subject);

	void subjectCleanup();

	String toString(Subject subject);

	boolean isAnnotated(Class<?> clazz);

	void checkPermission(Class<?> clazz);
	
}

package org.summerclouds.common.core.security;

import java.util.Locale;

public class DummySecurity implements ISecurity {

	public static final ISubject SUBJECT = new DummySubject();
	public static final ISubjectEnvironment SUBJECT_ENVIRONMENT = new DummySubjectEnvironment();

	@Override
	public ISubject getCurrent() {
		return SUBJECT;
	}

	@Override
	public void subjectCleanup() {
	}

	@Override
	public ISubjectEnvironment asSubject(String username) {
		return SUBJECT_ENVIRONMENT;
	}

	@Override
	public ISubjectEnvironment asSubject(ISubject subject) {
		return SUBJECT_ENVIRONMENT;
	}

	@Override
	public String getAdminName() {
		return "admin";
	}

	@Override
	public boolean hasPermission(Class<?> clazz, String action, String instance) {
		return true;
	}

	@Override
	public boolean hasPermission(String ace) {
		return true;
	}

	@Override
	public boolean hasPermission(String clazz, String action, String instance) {
		return true;
	}

	@Override
	public boolean hasPermission(ISubject subject, Class<?> clazz, String action, String instance) {
		return true;
	}

	@Override
	public boolean hasPermission(ISubject subject, String ace) {
		return true;
	}

	@Override
	public boolean hasPermission(ISubject subject, String clazz, String action, String instance) {
		return true;
	}

	@Override
	public boolean hasRole(String role) {
		return true;
	}

	@Override
	public boolean hasRole(ISubject subject, String role) {
		return true;
	}

	@Override
	public boolean isAdmin() {
		return false;
	}

	@Override
	public boolean isAdmin(ISubject subject) {
		return false;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setLocale(Locale locale) {
		
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public Object getSessionAttribute(String key) {
		return null;
	}

	@Override
	public Object getSessionAttribute(String key, Object def) {
		return def;
	}

	@Override
	public void setSessionAttribute(String key, Object value) {
		
	}

	@Override
	public void touch() {
		
	}

	@Override
	public ISubject getSubject(String username) {
		return null;
	}

	@Override
	public boolean hasPermission(Class<?> clazz) {
		return true;
	}

}

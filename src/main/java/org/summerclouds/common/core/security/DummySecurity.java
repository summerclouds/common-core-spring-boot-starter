package org.summerclouds.common.core.security;

import java.util.List;
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
	public boolean hasPermission(Class<?> object, String action, String instance) {
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

	@Override
	public boolean hasPermission(ISubject subject, String ace) {
		return true;
	}

	@Override
	public boolean hasPermission(ISubject subject, String domain, String action, String instance) {
		return true;
	}

	@Override
	public boolean hasPermissionByList(List<String> map, ISubject account, String objectIdent) {
		return true;
	}

	@Override
	public boolean hasPermissionByList(String list, ISubject account, String objectIdent) {
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

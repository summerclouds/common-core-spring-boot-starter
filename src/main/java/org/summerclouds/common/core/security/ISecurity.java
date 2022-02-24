package org.summerclouds.common.core.security;

import java.util.Locale;

import org.summerclouds.common.core.activator.DefaultImplementation;

@DefaultImplementation(DummySecurity.class)
public interface ISecurity {

	ISubject getCurrent();

	ISubject getSubject(String username);
	
	void subjectCleanup();

	ISubjectEnvironment asSubject(String username);
	
	ISubjectEnvironment asSubject(ISubject subject);
	
	String getAdminName();
	
	boolean hasPermission(Class<?> object, String action, String instance);
	
	boolean hasPermission(String ace);
	
	boolean hasPermission(String object, String action, String instance);
	
	boolean hasPermission(
            ISubject subject, Class<?> object, String action, String instance);
	
	boolean hasPermission(ISubject subject, String ace);
	
	boolean hasPermission(
            ISubject subject, String object, String action, String instance);
	
	boolean hasRole(String role);
	
	boolean hasRole(ISubject subject, String role);
	
	boolean isAdmin();
	
	boolean isAdmin(ISubject subject);
	
	boolean isAuthenticated();
	
	void setLocale(Locale locale);
		
	Locale getLocale();
	
	Object getSessionAttribute(String key);
	
	Object getSessionAttribute(String key, Object def);
	
	void setSessionAttribute(String key, Object value);
		
	void touch();

	/**
	 * Check if the current user has access to the class check by annotations.
	 * 
	 * @param clazz
	 * @return
	 */
	boolean hasPermission(Class<?> clazz);
	
	
}

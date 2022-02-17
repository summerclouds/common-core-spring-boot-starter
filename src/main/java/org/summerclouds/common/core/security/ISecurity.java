package org.summerclouds.common.core.security;

import java.util.List;
import java.util.Locale;

public interface ISecurity {

	ISubject getCurrent();

	ISubject getSubject(String username);
	
	void subjectCleanup();

//	boolean isAnnotated(Class<?> clazz);
//
//	void checkPermission(Class<?> clazz);
	
	ISubjectEnvironment asSubject(String username);
	
	ISubjectEnvironment asSubject(ISubject subject);
	
	default ISubjectEnvironment asAdmin() {
		return asSubject(getAdminName());
	}

	String getAdminName();
	
	boolean hasPermission(Class<?> object, String action, String instance);
	
	boolean hasPermission(String ace);
	
	boolean hasPermission(String object, String action, String instance);
	
	boolean hasPermission(
            ISubject subject, Class<?> object, String action, String instance);
	
	boolean hasPermission(ISubject subject, String ace);
	
	boolean hasPermission(
            ISubject subject, String domain, String action, String instance);
	
	boolean hasPermissionByList(List<String> map, ISubject account, String objectIdent);
	
	boolean hasPermissionByList(String list, ISubject account, String objectIdent);
	
//	boolean hasPermission(ISubject subject, Annotation[] annotations);
//	
//	boolean hasPermission(ISubject subject, Class<?> clazz);
//	
//	boolean hasPermission(ISubject subject, Method method);
	
	boolean hasRole(String role);
	
	boolean hasRole(ISubject subject, String role);
	
	boolean isAdmin();
	
	boolean isAdmin(ISubject subject);
	
//	boolean isAnnotated(Method method);
	
	boolean isAuthenticated();
	
//	boolean isPermitted(
//            List<String> rules, Class<?> permission, String level, Object instance);
//	
//	boolean isPermitted(
//            List<String> rules, String permission, String level, String instance);
//	
//	boolean isPermitted(String ace);
//	
//	boolean isPermitted(String permission, String level, String instance);
//	
//	boolean isPermitted(ISubject subject, String ace);
//	
//	boolean isPermitted(
//            ISubject subject, String permission, String level, String instance);
	
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

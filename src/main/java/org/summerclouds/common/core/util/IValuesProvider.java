package org.summerclouds.common.core.util;

public interface IValuesProvider {

	Object get(String key);
	
	default Object getOrDefault(String key, Object def) {
		Object value = get(key);
		return value == null ? def : value;
	}
}

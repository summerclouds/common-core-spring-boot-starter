package org.summerclouds.common.core.tracing;

public interface ISpan {

	void exception(Throwable exception);
	
	void setError(String error);
	
	void setTag(String key, String value);
	
	default void setError(Throwable exception) {
		setError(exception.toString());
		exception(exception);
	}

	void log(String message);
}

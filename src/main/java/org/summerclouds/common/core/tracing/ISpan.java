package org.summerclouds.common.core.tracing;

public interface ISpan {

	void exception(Throwable exception);
	
	void setError(String error);
	
	void tag(String key, String value);
	
	default void setError(Throwable exception) {
		setError(exception.toString());
		exception(exception);
	}
}

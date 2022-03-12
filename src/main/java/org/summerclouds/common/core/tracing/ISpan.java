package org.summerclouds.common.core.tracing;

public interface ISpan {

	void record(Throwable exception);
	
	void setError(String error);
	
	default void setError(Throwable exception) {
		setError(exception.toString());
		record(exception);
	}
}

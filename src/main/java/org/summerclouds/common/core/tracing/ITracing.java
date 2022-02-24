package org.summerclouds.common.core.tracing;

public interface ITracing {

	ISpan current();

	IScope enter(ISpan span, String name, Object ... keyValue);

	void cleanup();

	IScope enter(String name, Object ... keyValue);

	String getCurrentId();

}

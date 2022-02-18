package org.summerclouds.common.core.tracing;

public interface ITracing {

	ISpan current();

	IScope enter(ISpan span, String name, String ... keyValue);

	void cleanup();

	IScope enter(String name, String ... keyValue);

}

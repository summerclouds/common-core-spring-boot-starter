package org.summerclouds.common.core.tracing;

import org.summerclouds.common.core.activator.DefaultImplementation;

@DefaultImplementation(DummyTracing.class)
public interface ITracing {

	ISpan current();

	IScope enter(ISpan span, String name, Object ... keyValue);

	void cleanup();

	IScope enter(String name, Object ... keyValue);

	String getCurrentId();

}

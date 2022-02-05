package org.sommerclouds.common.core.tracing;

public interface ITracing {

	ISpan current();

	IScope enter(ISpan span, String string, String string2, String string3, String string4, String string5);

	void cleanup();

	IScope enter(ISpan span, String name);

}

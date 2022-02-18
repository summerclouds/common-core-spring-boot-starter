package org.summerclouds.common.core.tracing;

public class DummyScope implements IScope {

	@Override
	public void close() {

	}

	@Override
	public ISpan getSpan() {
		return DummyTracing.DUMMY_SPAN;
	}

}

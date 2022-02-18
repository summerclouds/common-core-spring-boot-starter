package org.summerclouds.common.core.tracing;

public class DummyTracing implements ITracing {

	static final ISpan DUMMY_SPAN = new DummySpan();
	static final IScope DUMMY_SCOPE = new DummyScope();

	@Override
	public ISpan current() {
		return DUMMY_SPAN;
	}

	@Override
	public IScope enter(ISpan span, String name, String... keyValue) {
		return DUMMY_SCOPE;
	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public IScope enter(String name, String... keyValue) {
		return DUMMY_SCOPE;
	}

}

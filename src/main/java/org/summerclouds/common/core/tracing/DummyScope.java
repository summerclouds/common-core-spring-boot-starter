package org.summerclouds.common.core.tracing;

public class DummyScope implements IScope {

	private ISpan span;

	public DummyScope() {
		this(DummyTracing.DUMMY_SPAN);
	}
	
	public DummyScope(ISpan span) {
		this.span = span;
	}

	@Override
	public void close() {

	}

	@Override
	public ISpan getSpan() {
		return span;
	}

}

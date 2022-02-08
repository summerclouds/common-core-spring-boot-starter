package org.summerclouds.common.core.tracing;

public class NoopTracing implements ITracing {

	private static final ISpan NO_SPAN = new NoSpan();
	private static final IScope NO_SCOPE = new NoScope();

	@Override
	public ISpan current() {
		return NO_SPAN;
	}

	@Override
	public IScope enter(ISpan span, String name, String... keyValue) {
		return NO_SCOPE;
	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public IScope enter(ISpan span, String name) {
		return NO_SCOPE;
	}

	@Override
	public IScope enter(String name, String... keyValue) {
		return NO_SCOPE;
	}

	
	private static class NoSpan implements ISpan {
		
	}
	
	private static class NoScope implements IScope {

		@Override
		public void close() {
			
		}
		
	}

}

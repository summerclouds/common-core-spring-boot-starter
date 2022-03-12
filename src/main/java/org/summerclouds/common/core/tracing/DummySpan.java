package org.summerclouds.common.core.tracing;

public class DummySpan implements ISpan {

	@Override
	public void record(Throwable exception) {
		
	}

	@Override
	public void setError(String error) {
		
	}

}

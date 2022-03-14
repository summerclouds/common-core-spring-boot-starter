package org.summerclouds.common.core.tracing;

public class DummySpan implements ISpan {

	private String name;

	public DummySpan() {
		name = "";
	}
	
	public DummySpan(String name) {
		this.name = name;
	}
	
	@Override
	public void exception(Throwable exception) {
		
	}

	@Override
	public void setError(String error) {
		
	}

	@Override
	public void tag(String key, String value) {
		
	}

	public String toString() {
		return name;
	}
	
}

package org.summerclouds.common.core.security;

public class DummySubjectEnvironment implements ISubjectEnvironment {

	@Override
	public ISubject getSubject() {
		return DummySecurity.SUBJECT;
	}

	@Override
	public void close() {

	}

}

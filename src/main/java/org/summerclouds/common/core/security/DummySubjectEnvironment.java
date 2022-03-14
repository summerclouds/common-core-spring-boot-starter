package org.summerclouds.common.core.security;

public class DummySubjectEnvironment implements ISubjectEnvironment {

	private ISubject subject;

	public DummySubjectEnvironment() {
		this(DummySecurity.SUBJECT);
	}
	
	public DummySubjectEnvironment(ISubject subject) {
		this.subject = subject;
	}
	
 	@Override
	public ISubject getSubject() {
		return subject;
	}

	@Override
	public void close() {

	}

}

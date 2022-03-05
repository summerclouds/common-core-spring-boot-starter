package org.summerclouds.common.core.crypt;

import org.summerclouds.common.core.error.DummyException;

public class DummyEncoder implements IPasswordEncoder {

	@Override
	public String encode(String plain, String secret) {
		return "";
	}

	@Override
	public String decode(String encoded, String secret) {
		throw new DummyException("try to encode a dummy password");
	}

	@Override
	public boolean validate(String plain, String encoded, String secret) {
		return false;
	}

}

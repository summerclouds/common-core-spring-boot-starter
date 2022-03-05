package org.summerclouds.common.core.crypt;

public class Rot13Encoder implements IPasswordEncoder {

	@Override
	public String encode(String plain, String secret) {
		return Rot13.encode13(plain);
	}

	@Override
	public String decode(String encoded, String secret) {
		return Rot13.decode13(encoded);
	}

	@Override
	public boolean validate(String plain, String encoded, String secret) {
		return encoded.equals(encode(plain, secret));
	}

}

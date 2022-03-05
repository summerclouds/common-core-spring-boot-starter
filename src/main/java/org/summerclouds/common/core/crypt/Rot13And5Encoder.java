package org.summerclouds.common.core.crypt;

public class Rot13And5Encoder implements IPasswordEncoder {

	@Override
	public String encode(String plain, String secret) {
		return Rot13.encode13And5(plain);
	}

	@Override
	public String decode(String encoded, String secret) {
		return Rot13.decode13And5(encoded);
	}

	@Override
	public boolean validate(String plain, String encoded, String secret) {
		return encoded.equals(encode(plain, secret));
	}

}

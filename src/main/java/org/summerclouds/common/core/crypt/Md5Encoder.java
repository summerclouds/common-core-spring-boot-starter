package org.summerclouds.common.core.crypt;

import org.summerclouds.common.core.error.NotSupportedException;

public class Md5Encoder implements IPasswordEncoder {

	@Override
	public String encode(String plain, String secret) {
		return MD5.hash(plain); // without salt
	}

	@Override
	public String decode(String encoded, String secret) {
		throw new NotSupportedException("can't decode md5 hashes");
	}

	@Override
	public boolean validate(String plain, String encoded, String secret) {
		return encoded.equals(encode(plain, secret));
	}

}

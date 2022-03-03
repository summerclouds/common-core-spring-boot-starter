package org.summerclouds.common.core.crypt;

import java.util.ArrayList;

import org.summerclouds.common.core.error.DummyException;
import org.summerclouds.common.core.error.NotSupportedException;
import org.summerclouds.common.core.tool.MPassword;

public class DefaultPassword implements IPassword {

	@Override
	public String[] supportedEncodings() {
		ArrayList<String> supported = new ArrayList<>();
		supported.add(MPassword.ROT13);
		supported.add(MPassword.ROT13AND10);
		supported.add(MPassword.DUMMY);
		supported.add(MPassword.HASH_MD5);
		return null;
	}

	@Override
	public String encode(String method, String plain, String secret) {
        if (plain == null) return null;
        switch (method) {
        case MPassword.ROT13:
        	return Rot13.encode13(plain);
        case MPassword.ROT13AND10:
        	return Rot13.encode13And10(plain);
        case MPassword.DUMMY:
        	return "";
        case MPassword.HASH_MD5:
        	return MD5.hash(plain);
        }
		throw new NotSupportedException("method {1} not found",method);
	}

	@Override
	public String decode(String method, String encoded, String secret) {
        if (encoded == null) return null;
        switch (method) {
        case MPassword.ROT13:
        	return Rot13.decode13(encoded);
        case MPassword.ROT13AND10:
        	return Rot13.decode13And10(encoded);
        case MPassword.DUMMY:
        	throw new DummyException("try to encode a dummy password");
        case MPassword.HASH_MD5:
        	throw new NotSupportedException("can't decode md5 hashes");
        }
		throw new NotSupportedException("method {1} not found",method);
	}

}

package org.summerclouds.common.core.crypt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.summerclouds.common.core.error.NotSupportedException;
import org.summerclouds.common.core.tool.MPassword;
import org.summerclouds.common.core.tool.MSpring;

public class DefaultPassword implements IPassword {

	private static Map<String, IPasswordEncoder> encodings = null;
	private Collection<String> keys;
	
	@Override
	public Collection<String> supportedEncodings() {
		init();
		return keys;
	}

	protected final synchronized void init() {
		if (encodings != null) return;
		encodings = new HashMap<>();
		encodings.put(MPassword.ROT13, new Rot13Encoder());
		encodings.put(MPassword.ROT13AND5, new Rot13And5Encoder());
		encodings.put(MPassword.DUMMY, new DummyEncoder());
		encodings.put(MPassword.MD5, new Md5Encoder());
		
		Map<String, IPasswordEncoder> map = MSpring.getBeansOfType(IPasswordEncoder.class);
		if (map != null)
			map.forEach((k,v) -> encodings.put(k.toLowerCase(), v)  );
		
		additionalEncodings(encodings);
		
		keys = Collections.unmodifiableCollection( encodings.keySet() );
		
	}

	protected void additionalEncodings(Map<String, IPasswordEncoder> encodings2) {
	}

	@Override
	public String encode(String method, String plain, String secret) {
        if (plain == null) return null;
		init();
        
		IPasswordEncoder encoder = encodings.get(method.toLowerCase());
		if (encoder == null)
			throw new NotSupportedException("method {1} not found",method);

		return encoder.encode(plain, secret);
	}

	@Override
	public String decode(String method, String encoded, String secret) {
        if (encoded == null) return null;
		init();
        
		IPasswordEncoder encoder = encodings.get(method.toLowerCase());
		if (encoder == null)
			throw new NotSupportedException("method {1} not found",method);

		return encoder.decode(encoded, secret);
	}

	@Override
	public boolean validate(String method, String plain, String encoded, String secret) {
        if (plain == null) return false;
		init();

		IPasswordEncoder encoder = encodings.get(method.toLowerCase());
		if (encoder == null)
			throw new NotSupportedException("method {1} not found",method);
		
		return encoder.validate(plain, encoded, secret);
	}

}

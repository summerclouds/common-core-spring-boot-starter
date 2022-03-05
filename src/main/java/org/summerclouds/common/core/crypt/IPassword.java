package org.summerclouds.common.core.crypt;

import java.util.Collection;

import org.summerclouds.common.core.activator.DefaultImplementation;

@DefaultImplementation(DefaultPassword.class)
public interface IPassword {

	Collection<String> supportedEncodings();
	
    String encode(String method, String plain, String secret);

    String decode(String method, String encoded, String secret);

    boolean validate(String method, String plain, String encoded, String secret);

}
